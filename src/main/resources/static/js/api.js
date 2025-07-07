// js/api.js

const API_BASE_URL = ''; // Altere se o seu backend estiver em outra porta/endereço

async function fetchData(url) {
    try {
        const response = await fetch(`${API_BASE_URL}${url}`);
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erro ${response.status}: ${errorText}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
        throw error; // Re-throw para ser tratado pela função chamadora
    }
}

async function postData(url, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${url}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erro ${response.status}: ${errorText}`);
        }
        return await response.text(); // Retorna texto para mensagens de sucesso
    } catch (error) {
        console.error('Erro ao enviar dados:', error);
        throw error;
    }
}

async function putData(url, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${url}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erro ${response.status}: ${errorText}`);
        }
        return await response.text();
    } catch (error) {
        console.error('Erro ao atualizar dados:', error);
        throw error;
    }
}

async function deleteData(url, id) {
    try {
        const response = await fetch(`${API_BASE_URL}${url}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(id) // Seu controller espera um Integer no body
        });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erro ${response.status}: ${errorText}`);
        }
        return await response.text();
    } catch (error) {
        console.error('Erro ao deletar dados:', error);
        throw error;
    }
}

// Helper para formatar data e hora para input[type="datetime-local"]
function formatDateTimeForInput(isoString) {
    if (!isoString) return '';
    const date = new Date(isoString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

// Helper para converter string do input para LocalDateTime (sem segundos, milissegundos)
function parseDateTimeFromInput(dateTimeString) {
    if (!dateTimeString) return null;
    const [datePart, timePart] = dateTimeString.split('T');
    const [year, month, day] = datePart.split('-').map(Number);
    const [hours, minutes] = timePart.split(':').map(Number);
    return new Date(year, month - 1, day, hours, minutes).toISOString(); // Retorna ISO string que o backend deve conseguir parsear
}


// Função para exibir mensagens na UI
function displayMessage(message, type = 'success') {
    const messageContainer = document.getElementById('message-container');
    if (!messageContainer) {
        console.warn('Elemento #message-container não encontrado.');
        return;
    }
    messageContainer.innerHTML = `<div class="message ${type}">${message}</div>`;
    setTimeout(() => {
        messageContainer.innerHTML = '';
    }, 5000); // Remove a mensagem após 5 segundos
}