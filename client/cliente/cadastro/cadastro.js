// JavaScript Feito por IA

document.getElementById('cadastroForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const nome = document.getElementById('nome').value;
    const cpf = document.getElementById('cpf').value;
    const password = document.getElementById('password').value;
    const nascimento = document.getElementById('nascimento').value;
    const msg = document.getElementById('msg');

    try {
        const res = await fetch('http://localhost:8080/api/pessoas/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, cpf, password, nascimento })
        });

        if (!res.ok) throw new Error(await res.text());

        msg.textContent = "Cadastro realizado com sucesso! Redirecionando...";
        setTimeout(() => {
            window.location.href = '../login/index.html';
        }, 2000);

    } catch (err) {
        msg.textContent = `Erro: ${err.message}`;
    }
});
