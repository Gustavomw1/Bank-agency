// JavaScript Feito por IA

const token = localStorage.getItem('token');
const cpf = localStorage.getItem('cpf');

if (!cpf || !token) {
    alert('Você precisa estar logado para acessar a dashboard.');
    window.location.href = '../login/index.html';
}

function authFetch(url, options = {}) {
    return fetch(url, {
        ...options,
        headers: {
            ...(options.headers || {}),
            'Authorization': `Bearer ${token}`
        }
    });
}

async function getPessoaId() {
    try {
        const res = await authFetch('http://localhost:8080/api/pessoas');
        if (!res.ok) throw new Error('Erro ao buscar pessoas');
        const pessoas = await res.json();
        const pessoa = pessoas.find(p => p.cpf === cpf);
        if (!pessoa) throw new Error('Pessoa não encontrada');
        return pessoa.id;
    } catch (err) {
        alert('Erro: ' + err.message);
        return null;
    }
}

async function carregarDashboard() {
    const id = await getPessoaId();
    if (!id) return;

    const saldoEl = document.getElementById('saldo');
    const transacoesEl = document.getElementById('transacoes');

    try {
        // Saldo
        const saldoRes = await authFetch(`http://localhost:8080/api/contas/${id}/saldo`);
        if (!saldoRes.ok) throw new Error('Erro ao obter saldo');
        const saldo = await saldoRes.text();
        saldoEl.textContent = `Saldo: R$ ${saldo}`;

        // Transações
        const transacoesRes = await authFetch(`http://localhost:8080/api/contas/${id}/transacoes`);
        if (!transacoesRes.ok) throw new Error('Erro ao buscar transações');
        const transacoes = await transacoesRes.json();

        transacoesEl.innerHTML = '';
        transacoes.forEach(t => {
            const li = document.createElement('li');
            li.textContent = `${t.tipo} - R$${t.valor} - ${new Date(t.data).toLocaleString('pt-BR')}`;
            transacoesEl.appendChild(li);
        });

    } catch (err) {
        saldoEl.textContent = `Erro: ${err.message}`;
    }
}

async function depositar() {
    const id = await getPessoaId();
    const valor = document.getElementById('depositoValor').value;
    if (!valor) return alert('Informe um valor válido.');

    try {
        const res = await authFetch(`http://localhost:8080/api/contas/${id}/depositar?valor=${valor}`, {
            method: 'POST'
        });
        if (!res.ok) throw new Error(await res.text());
        carregarDashboard();
    } catch (err) {
        alert('Erro ao depositar: ' + err.message);
    }
}

async function sacar() {
    const id = await getPessoaId();
    const valor = document.getElementById('saqueValor').value;
    if (!valor) return alert('Informe um valor válido.');

    try {
        const res = await authFetch(`http://localhost:8080/api/contas/${id}/sacar?valor=${valor}`, {
            method: 'POST'
        });
        if (!res.ok) throw new Error(await res.text());
        carregarDashboard();
    } catch (err) {
        alert('Erro ao sacar: ' + err.message);
    }
}

async function transferir() {
    const origemId = await getPessoaId();
    const destinoCpf = document.getElementById('destinoCpf').value;
    const valor = document.getElementById('valorTransferencia').value;

    if (!valor || !destinoCpf) return alert('Informe valor e CPF de destino.');

    try {
        // Buscar pessoas para encontrar o ID do CPF destino
        const pessoasRes = await authFetch('http://localhost:8080/api/pessoas');
        if (!pessoasRes.ok) throw new Error('Erro ao buscar pessoas');
        const pessoas = await pessoasRes.json();
        const destinoPessoa = pessoas.find(p => p.cpf === destinoCpf);
        if (!destinoPessoa) throw new Error('CPF de destino não encontrado');

        const destinoId = destinoPessoa.id;

        const res = await authFetch(`http://localhost:8080/api/contas/${origemId}/transferir?destinoId=${destinoId}&valor=${valor}`, {
            method: 'POST'
        });
        if (!res.ok) throw new Error(await res.text());
        carregarDashboard();
    } catch (err) {
        alert('Erro ao transferir: ' + err.message);
    }
}


async function mostrarUsuario() {
    try {
        const res = await authFetch('http://localhost:8080/api/pessoas');
        const pessoas = await res.json();
        const pessoa = pessoas.find(p => p.cpf === cpf);
        if (pessoa) {
            document.getElementById('user-info').textContent = `ID: ${pessoa.id} | Nome: ${pessoa.nome}`;
        }
    } catch (err) {
        console.error('Erro ao exibir dados do usuário:', err);
    }
}

carregarDashboard();
mostrarUsuario();
