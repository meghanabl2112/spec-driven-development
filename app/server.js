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

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));