// JavaScript Feito por IA

const token = localStorage.getItem('token');
const cpf = localStorage.getItem('cpf');

if (!cpf || !token) {
    mostrarMensagem('Você precisa estar logado para acessar o sistema.');
    setTimeout(() => {
        window.location.href = '../login/index.html';
    }, 2000);
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
        mostrarMensagem('Erro: ' + err.message);
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
        mostrarMensagem('Informe um valor válido para depósito.');
        return;
    }

    try {
        const res = await authFetch(`http://localhost:8080/api/contas/${id}/depositar?valor=${valor}`, {
            method: 'POST'
        });

        if (!res.ok) throw new Error(await res.text());

        mostrarMensagem('Depósito realizado com sucesso!');
        setTimeout(() => {
            window.location.href = '../dashboard/index.html';
        }, 2000);
    } catch (err) {
        mostrarMensagem('Erro ao depositar: ' + err.message);
    }
}

async function sacar() {
    const id = await getPessoaId();
    const valor = document.getElementById('saqueValor')?.value;

    if (!valor || valor <= 0) {
        mostrarMensagem('Informe um valor válido para saque.');
        return;
    }

    try {
        const res = await authFetch(`http://localhost:8080/api/contas/${id}/sacar?valor=${valor}`, {
            method: 'POST'
        });

        if (!res.ok) throw new Error(await res.text());

        mostrarMensagem('Saque realizado com sucesso!');
        setTimeout(() => {
            window.location.href = '../dashboard/index.html';
        }, 2000);
    } catch (err) {
        mostrarMensagem('Erro ao sacar: ' + err.message);
    }
}

async function transferir() {
    const origemCpf = localStorage.getItem('cpf')?.trim();
    const destinoCpf = document.getElementById('destinoCpf')?.value.trim();
    const valor = parseFloat(document.getElementById('valorTransferencia')?.value);

    if (!destinoCpf || isNaN(valor)) {
        mostrarMensagem('Informe o CPF de destino e um valor numérico.');
        return;
    }

    if (valor <= 0) {
        mostrarMensagem('O valor da transferência deve ser maior que zero.');
        return;
    }

    if (destinoCpf === origemCpf) {
        mostrarMensagem('Você não pode transferir para sua própria conta.');
        return;
    }

    try {
        const res = await authFetch(`http://localhost:8080/api/contas/transferir?origemCpf=${origemCpf}&destinoCpf=${destinoCpf}&valor=${valor}`, {
            method: 'POST'
        });

        if (!res.ok) throw new Error(await res.text());

        mostrarMensagem('Transferência realizada com sucesso!');
        setTimeout(() => {
            window.location.href = '../dashboard/index.html';
        }, 2000);
    } catch (err) {
        mostrarMensagem('Erro ao transferir: ' + err.message);
    }
}

function mostrarMensagem(texto, tempo = 3000) {
    const msg = document.getElementById('msg');
    const container = document.getElementById('msgContainer');

    if (!msg || !container) return;

    msg.textContent = texto;
    container.style.display = 'block';

    setTimeout(() => {
        container.style.display = 'none';
        msg.textContent = '';
    }, tempo);
}

mostrarUsuario();
carregarDashboard();
