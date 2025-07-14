// JavaScript feito por IA

const token = localStorage.getItem('token');
const cpf = localStorage.getItem('cpf');

if (!cpf || !token) {
    alert('Você precisa estar logado para acessar o sistema.');
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

async function mostrarUsuario() {
    try {
        const res = await authFetch('http://localhost:8080/api/pessoas');
        const pessoas = await res.json();
        const pessoa = pessoas.find(p => p.cpf === cpf);
        if (pessoa) {
            const userId = document.getElementById('user-id');
            const userName = document.getElementById('user-name');
            if (userId) userId.textContent = `ID: ${pessoa.id}`;
            if (userName) userName.textContent = `Nome: ${pessoa.nome}`;
        }
    } catch (err) {
        console.error('Erro ao exibir dados do usuário:', err);
    }
}

async function carregarDashboard() {
    const id = await getPessoaId();
    if (!id) return;

    const saldoEl = document.getElementById('saldo');
    const transacoesEl = document.getElementById('transacoes');

    try {
        if (saldoEl) {
            const saldoRes = await authFetch(`http://localhost:8080/api/contas/${id}/saldo`);
            if (!saldoRes.ok) throw new Error('Erro ao obter saldo');
            const saldo = await saldoRes.text();
            saldoEl.textContent = `Saldo: R$ ${saldo}`;
        }

        if (transacoesEl) {
            const transacoesRes = await authFetch(`http://localhost:8080/api/contas/${id}/transacoes`);
            if (!transacoesRes.ok) throw new Error('Erro ao buscar transações');
            const transacoes = await transacoesRes.json();

            transacoesEl.innerHTML = '';
            transacoes.forEach(t => {
                const li = document.createElement('li');
                li.textContent = `${t.tipo} - R$${t.valor} - ${new Date(t.data).toLocaleString('pt-BR')}`;
                transacoesEl.appendChild(li);
            });
        }
    } catch (err) {
        if (saldoEl) saldoEl.textContent = `Erro: ${err.message}`;
        if (transacoesEl) transacoesEl.innerHTML = `<li>Erro: ${err.message}</li>`;
    }
}

async function depositar() {
    const id = await getPessoaId();
    const valor = document.getElementById('depositoValor')?.value;
    if (!valor || valor <= 0) {
        return alert('Informe um valor válido para depósito.');
    }

    try {
        const res = await authFetch(`http://localhost:8080/api/contas/${id}/depositar?valor=${valor}`, {
            method: 'POST'
        });
        if (!res.ok) throw new Error(await res.text());
        alert('Depósito realizado com sucesso!');
        window.location.href = '../dashboard/index.html';
    } catch (err) {
        alert('Erro ao depositar: ' + err.message);
    }
}

async function sacar() {
    const id = await getPessoaId();
    const valor = document.getElementById('saqueValor')?.value;
    if (!valor || valor <= 0) {
        return alert('Informe um valor válido para saque.');
    }

    try {
        const res = await authFetch(`http://localhost:8080/api/contas/${id}/sacar?valor=${valor}`, {
            method: 'POST'
        });
        if (!res.ok) throw new Error(await res.text());
        alert('Saque realizado com sucesso!');
        window.location.href = '../dashboard/index.html';
    } catch (err) {
        alert('Erro ao sacar: ' + err.message);
    }
}

async function transferir() {
    const origemCpf = localStorage.getItem('cpf');
    const destinoCpf = document.getElementById('destinoCpf')?.value;
    const valor = document.getElementById('valorTransferencia')?.value;

    if (!destinoCpf || !valor || valor <= 0) {
        return alert('Informe CPF de destino e valor válido.');
    }

    try {
        const res = await authFetch(`http://localhost:8080/api/contas/transferir?origemCpf=${origemCpf}&destinoCpf=${destinoCpf}&valor=${valor}`, {
            method: 'POST'
        });

        if (!res.ok) throw new Error(await res.text());

        alert('Transferência realizada com sucesso!');
        window.location.href = '../dashboard/index.html';
    } catch (err) {
        alert('Erro ao transferir: ' + err.message);
    }
}

mostrarUsuario();
carregarDashboard();
