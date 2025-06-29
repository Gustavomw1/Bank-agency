// JavaScript Feito por IA

document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const cpf = document.getElementById('cpf').value;
    const password = document.getElementById('password').value;
    const msg = document.getElementById('msg');

    try {
        const res = await fetch('http://localhost:8080/api/pessoas/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ cpf, password })
        });

        if (!res.ok) throw new Error(await res.text());

        const data = await res.json();
        localStorage.setItem('token', data.token);
        localStorage.setItem('cpf', cpf);
        msg.textContent = 'Login bem-sucedido! Redirecionando...';
        setTimeout(() => {
            window.location.href = '../dashboard/index.html';
        }, 1500);

    } catch (err) {
        msg.textContent = `Erro: ${err.message}`;
    }
});
