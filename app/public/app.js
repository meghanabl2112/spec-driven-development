document.addEventListener('DOMContentLoaded', () => {
    const accountsList = document.getElementById('accountsList');
    const newAccountForm = document.getElementById('newAccountForm');

    fetchAccounts();

    async function fetchAccounts() {
        const res = await fetch('/api/accounts');
        const accounts = await res.json();
        accountsList.innerHTML = '';
        accounts.forEach(acc => {
            accountsList.innerHTML += `
                <div style="border:1px solid #ccc; margin:10px; padding:10px;">
                    <strong>${acc.type} (${acc.id})</strong><br>
                    Name: ${acc.customerName}<br>
                    Balance: $${acc.balance.toFixed(2)}
                </div>`;
        });
    }

    newAccountForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const payload = {
            customerName: document.getElementById('accName').value,
            type: document.getElementById('accType').value,
            initialDeposit: document.getElementById('accDeposit').value
        };
        await fetch('/api/accounts', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        newAccountForm.reset();
        fetchAccounts();
    });
});