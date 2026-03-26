const express = require('express');
const path = require('path');
const { v4: uuidv4 } = require('uuid');

const app = express();
const PORT = 3000;

app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

let accounts = [];

// Get all accounts
app.get('/api/accounts', (req, res) => {
    res.json(accounts);
});

// Create a new account
app.post('/api/accounts', (req, res) => {
    const { customerName, type, initialDeposit } = req.body;
    const newAccount = {
        id: `acc-${Date.now()}`,
        customerName,
        type,
        balance: parseFloat(initialDeposit),
        transactions: [],
        buckets: []
    };
    accounts.push(newAccount);
    res.status(201).json(newAccount);
});

// Perform a transaction
app.post('/api/accounts/:id/transactions', (req, res) => {
    const { type, amount } = req.body;
    const acc = accounts.find(a => a.id === req.params.id);
    const transAmount = parseFloat(amount);

    if (!acc) return res.status(404).json({ error: 'Account not found' });

    if (type === 'Withdrawal' && acc.balance < transAmount) {
        return res.status(422).json({ error: 'Insufficient funds.' });
    }
    
    acc.balance += (type === 'Deposit') ? transAmount : -transAmount;
    
    // Log the transaction
    acc.transactions.push({ type, amount: transAmount, date: new Date().toISOString() });
    
    res.status(201).json({ newBalance: acc.balance });
});

// Add a Savings Bucket
app.post('/api/accounts/:id/buckets', (req, res) => {
    const acc = accounts.find(a => a.id === req.params.id);
    if (!acc || acc.type !== 'Savings') {
        return res.status(400).json({ error: 'Buckets require a Savings account.' });
    }
    
    acc.buckets.push({
        id: `bucket-${Date.now()}`,
        name: req.body.name,
        target: parseFloat(req.body.target),
        balance: 0
    });
    
    res.status(201).json(acc.buckets);
});

// Auto-Distribute Unallocated Funds
app.post('/api/accounts/:id/distribute', (req, res) => {
    const acc = accounts.find(a => a.id === req.params.id);
    if (!acc || acc.type !== 'Savings') return res.status(400).json({ error: 'Invalid account.' });

    let allocated = acc.buckets.reduce((sum, b) => sum + b.balance, 0);
    let unallocated = acc.balance - allocated;

    for (let bucket of acc.buckets) {
        let deficit = bucket.target - bucket.balance;
        if (deficit > 0 && unallocated > 0) {
            let amountToAdd = Math.min(deficit, unallocated);
            bucket.balance += amountToAdd;
            unallocated -= amountToAdd;
        }
    }

    res.json({ newBuckets: acc.buckets, unallocatedRemaining: unallocated });
});

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));