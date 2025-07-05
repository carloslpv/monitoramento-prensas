// js/dashboard.js

// Variáveis globais para armazenar os dados brutos
let allMachines = [];
let allHistoricoFalhas = [];
let allHistoricoManutencao = [];
let allTelemetrias = [];

// Armazena as instâncias dos gráficos para poder destruí-los e recriá-los
const charts = {};

document.addEventListener('DOMContentLoaded', async () => {
    // Configura o Moment.js para português (Brasil)
    moment.locale('pt-br');

    await loadInitialData();
    await populateMachineSelect();

    // Adiciona listeners para o seletor de máquinas e filtro de data
    document.getElementById('apply-filter').addEventListener('click', applyDashboardFilters);
    document.getElementById('machine-select').addEventListener('change', applyDashboardFilters); // Aplica ao mudar a máquina também

    // Define datas padrão (últimos 30 dias)
    const endDateInput = document.getElementById('end-date');
    const startDateInput = document.getElementById('start-date');

    const today = new Date();
    const thirtyDaysAgo = new Date();
    thirtyDaysAgo.setDate(today.getDate() - 30);

    endDateInput.value = today.toISOString().split('T')[0];
    startDateInput.value = thirtyDaysAgo.toISOString().split('T')[0];

    // Renderiza o dashboard inicialmente com todas as máquinas e filtro de data padrão
    applyDashboardFilters();
});

async function loadInitialData() {
    try {
        // Carrega todos os dados necessários para o dashboard
        [allMachines, allHistoricoFalhas, allHistoricoManutencao, allTelemetrias] = await Promise.all([
            fetchData('/maquina/all'),
            fetchData('/historicoFalhas/all'),
            fetchData('/historico-manutencao/all'),
            fetchData('/telemetria/all')
        ]);
        // Garante que as datas sejam objetos Date para facilitar manipulação
        allHistoricoFalhas.forEach(f => f.dataFalha = new Date(f.dataFalha));
        allHistoricoManutencao.forEach(m => m.dataHoraManutencao = new Date(m.dataHoraManutencao));
        allTelemetrias.forEach(t => t.dataHoraColeta = new Date(t.dataHoraColeta));

    } catch (error) {
        console.error('Erro ao carregar dados iniciais para o dashboard:', error);
        displayMessage(`Erro ao carregar dados iniciais para o dashboard: ${error.message}`, 'error');
    }
}

async function populateMachineSelect() {
    const selectElement = document.getElementById('machine-select');
    // Limpa opções existentes, exceto a primeira ("Todas as Máquinas")
    selectElement.querySelectorAll('option:not([value="all"])').forEach(option => option.remove());

    if (allMachines.length > 0) {
        allMachines.forEach(machine => {
            const option = document.createElement('option');
            option.value = machine.id;
            option.textContent = machine.nome;
            selectElement.appendChild(option);
        });
    }
}

function applyDashboardFilters() {
    const selectedMachineId = document.getElementById('machine-select').value;
    const startDate = document.getElementById('start-date').value ? new Date(document.getElementById('start-date').value + 'T00:00:00') : null;
    const endDate = document.getElementById('end-date').value ? new Date(document.getElementById('end-date').value + 'T23:59:59') : null;

    // Filtra por máquina
    let filteredFalhas = selectedMachineId === 'all' ? allHistoricoFalhas : allHistoricoFalhas.filter(f => f.idMaquina === parseInt(selectedMachineId));
    let filteredManutencoes = selectedMachineId === 'all' ? allHistoricoManutencao : allHistoricoManutencao.filter(m => m.idMaquina === parseInt(selectedMachineId));
    let filteredTelemetrias = selectedMachineId === 'all' ? allTelemetrias : allTelemetrias.filter(t => t.idMaquina === parseInt(selectedMachineId));

    // Filtra por data
    if (startDate) {
        filteredFalhas = filteredFalhas.filter(f => f.dataFalha >= startDate);
        filteredManutencoes = filteredManutencoes.filter(m => m.dataHoraManutencao >= startDate);
        filteredTelemetrias = filteredTelemetrias.filter(t => t.dataHoraColeta >= startDate);
    }
    if (endDate) {
        filteredFalhas = filteredFalhas.filter(f => f.dataFalha <= endDate);
        filteredManutencoes = filteredManutencoes.filter(m => m.dataHoraManutencao <= endDate);
        filteredTelemetrias = filteredTelemetrias.filter(t => t.dataHoraColeta <= endDate);
    }

    renderDashboard(parseInt(selectedMachineId) || null, filteredFalhas, filteredManutencoes, filteredTelemetrias);
}

/**
 * Renderiza todos os gráficos do dashboard, aplicando os dados filtrados.
 * @param {number|null} selectedMachineId - O ID da máquina selecionada, ou null para todas as máquinas.
 * @param {Array} filteredFalhas - Dados de falhas já filtrados.
 * @param {Array} filteredManutencoes - Dados de manutenção já filtrados.
 * @param {Array} filteredTelemetrias - Dados de telemetria já filtrados.
 */
function renderDashboard(selectedMachineId, filteredFalhas, filteredManutencoes, filteredTelemetrias) {
    // Destruir gráficos existentes antes de redesenhar
    Object.values(charts).forEach(chart => {
        if (chart) chart.destroy();
    });

    // Renderizar cards e gráficos
    renderTotalMaquinas(allMachines); // Total de máquinas é sempre global

    // Novos Cards de Métricas Rápidas
    renderLatestMetricsCards(selectedMachineId, filteredTelemetrias);

    renderFalhasPorTipoChart(filteredFalhas); // Agora consolida falhas
    renderTempoManutencaoChart(filteredManutencoes, allMachines); // Passa allMachines para mapear nomes
    renderMaquinasStatusChart(filteredTelemetrias, selectedMachineId); // Precisa do machineId para lógica de "todas" vs "uma"
    renderTemperaturaOleoChart(filteredTelemetrias, allMachines);
    renderCiclosOperacaoChart(filteredTelemetrias, selectedMachineId); // Ciclos do dia
    renderTelemetryTrendsChart(filteredTelemetrias);
    renderSensorNivelBaixoChart(filteredTelemetrias);
    renderVibracaoMediaChart(filteredTelemetrias, allMachines);

    // Novos gráficos e informações adicionais
    renderFalhasPorDataChart(filteredFalhas); // Novo gráfico
    renderTempoManutencaoPorMesChart(filteredManutencoes); // Novo gráfico
    renderTop5FalhasChart(filteredFalhas); // Novo gráfico
}

// --- Funções de Renderização de Cards e Gráficos ---

function renderTotalMaquinas(maquinas) {
    const totalMaquinasElement = document.getElementById('total-maquinas');
    if (totalMaquinasElement) {
        totalMaquinasElement.textContent = maquinas.length;
    }
}

function renderLatestMetricsCards(selectedMachineId, telemetrias) {
    const latestPressureEl = document.getElementById('latest-pressure');
    const avgOilTempEl = document.getElementById('avg-oil-temp');
    const latestVibrationEl = document.getElementById('latest-vibration');

    if (selectedMachineId === null) {
        latestPressureEl.textContent = 'N/A';
        avgOilTempEl.textContent = 'N/A';
        latestVibrationEl.textContent = 'N/A';
        return;
    }

    const latestReading = telemetrias
        .sort((a, b) => b.dataHoraColeta - a.dataHoraColeta)[0]; // Já são Date objects

    if (latestReading) {
        latestPressureEl.textContent = latestReading.pressaoHidraulica !== undefined && latestReading.pressaoHidraulica !== null
            ? `${latestReading.pressaoHidraulica.toFixed(2)} Bar`
            : 'N/A';
        latestVibrationEl.textContent = latestReading.vibracao !== undefined && latestReading.vibracao !== null
            ? `${latestReading.vibracao.toFixed(2)} Hz`
            : 'N/A';

        const last5Temperatures = telemetrias
            .filter(t => t.temperaturaOleo !== undefined && t.temperaturaOleo !== null)
            .sort((a, b) => b.dataHoraColeta - a.dataHoraColeta)
            .slice(0, 5)
            .map(t => t.temperaturaOleo);

        const avgLast5Temp = last5Temperatures.length > 0
            ? (last5Temperatures.reduce((sum, temp) => sum + temp, 0) / last5Temperatures.length).toFixed(2)
            : 'N/A';
        avgOilTempEl.textContent = `${avgLast5Temp} °C`;

    } else {
        latestPressureEl.textContent = 'N/A';
        avgOilTempEl.textContent = 'N/A';
        latestVibrationEl.textContent = 'N/A';
    }
}


function renderFalhasPorTipoChart(historicoFalhas) {
    const ctx = document.getElementById('falhasPorTipoChart').getContext('2d');
    const falhasPorTipo = historicoFalhas.reduce((acc, falha) => {
        // Consolida falhas com o mesmo nome
        const tipoFalhaNormalizado = falha.tipoFalha.trim();
        acc[tipoFalhaNormalizado] = (acc[tipoFalhaNormalizado] || 0) + 1;
        return acc;
    }, {});

    charts.falhasPorTipoChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Object.keys(falhasPorTipo),
            datasets: [{
                label: 'Número de Falhas',
                data: Object.values(falhasPorTipo),
                backgroundColor: 'rgba(255, 99, 132, 0.6)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Quantidade'
                    },
                    ticks: {
                        precision: 0
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Tipo de Falha'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: historicoFalhas.length === 0 ? 'Nenhuma falha para exibir' : 'Falhas Recentes (Por Tipo)'
                }
            }
        }
    });
}

function renderTempoManutencaoChart(historicoManutencao, maquinas) {
    const ctx = document.getElementById('tempoManutencaoChart').getContext('2d');
    const maquinasMap = new Map(maquinas.map(m => [m.id, m.nome]));

    const tempoPorMaquina = historicoManutencao.reduce((acc, manutencao) => {
        const maquinaNome = maquinasMap.get(manutencao.idMaquina) || `Máquina ID ${manutencao.idMaquina}`;
        acc[maquinaNome] = (acc[maquinaNome] || 0) + manutencao.tempoManutencaoMin;
        return acc;
    }, {});

    charts.tempoManutencaoChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: Object.keys(tempoPorMaquina),
            datasets: [{
                label: 'Tempo de Manutenção (Minutos)',
                data: Object.values(tempoPorMaquina),
                backgroundColor: [
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(153, 102, 255, 0.6)',
                    'rgba(255, 159, 64, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(201, 203, 207, 0.6)',
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(255, 205, 86, 0.6)'
                ],
                borderColor: [
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(201, 203, 207, 1)',
                    'rgba(255, 99, 132, 1)',
                    'rgba(255, 205, 86, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'right',
                },
                title: {
                    display: true,
                    text: historicoManutencao.length === 0 ? 'Nenhuma manutenção para exibir' : 'Tempo Total de Manutenção por Máquina (Minutos)'
                }
            }
        }
    });
}

function renderMaquinasStatusChart(telemetrias, selectedMachineId) {
    const ctx = document.getElementById('maquinasStatusChart').getContext('2d');

    let statusCounts = { ligadas: 0, desligadas: 0 };
    let chartTitle = 'Status Atual das Máquinas';

    if (selectedMachineId !== null) {
        // Se uma máquina específica for selecionada, mostre o status dela
        const latestReading = telemetrias
            .filter(t => t.idMaquina === selectedMachineId)
            .sort((a, b) => b.dataHoraColeta - a.dataHoraColeta)[0];

        if (latestReading) {
            if (latestReading.maquinaLigada) {
                statusCounts.ligadas = 1;
            } else {
                statusCounts.desligadas = 1;
            }
            chartTitle = `Status da Máquina ID ${selectedMachineId}`;
        } else {
            // Nenhuma telemetria para a máquina selecionada
            statusCounts = { ligadas: 0, desligadas: 0 };
            chartTitle = `Nenhum dado de status para a Máquina ID ${selectedMachineId}`;
        }

    } else {
        // Se "Todas as Máquinas" for selecionado, agregue o status mais recente de cada máquina
        const latestStatusPerMachine = {};
        telemetrias.forEach(t => {
            if (!latestStatusPerMachine[t.idMaquina] || t.dataHoraColeta > latestStatusPerMachine[t.idMaquina].dataHoraColeta) {
                latestStatusPerMachine[t.idMaquina] = t;
            }
        });

        Object.values(latestStatusPerMachine).forEach(telemetria => {
            if (telemetria.maquinaLigada) {
                statusCounts.ligadas++;
            } else {
                statusCounts.desligadas++;
            }
        });
    }

    charts.maquinasStatusChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Ligadas', 'Desligadas'],
            datasets: [{
                data: [statusCounts.ligadas, statusCounts.desligadas],
                backgroundColor: [
                    'rgba(40, 167, 69, 0.6)', // Green for Ligadas
                    'rgba(220, 53, 69, 0.6)'  // Red for Desligadas
                ],
                borderColor: [
                    'rgba(40, 167, 69, 1)',
                    'rgba(220, 53, 69, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: chartTitle
                }
            }
        }
    });
}


function renderTemperaturaOleoChart(telemetrias, maquinas) {
    const ctx = document.getElementById('temperaturaOleoChart').getContext('2d');
    const maquinasMap = new Map(maquinas.map(m => [m.id, m.nome]));

    const tempSum = {};
    const tempCount = {};

    telemetrias.forEach(t => {
        if (t.temperaturaOleo !== undefined && t.temperaturaOleo !== null) {
            tempSum[t.idMaquina] = (tempSum[t.idMaquina] || 0) + t.temperaturaOleo;
            tempCount[t.idMaquina] = (tempCount[t.idMaquina] || 0) + 1;
        }
    });

    const avgTemperatures = {};
    for (const idMaquina in tempSum) {
        const maquinaNome = maquinasMap.get(parseInt(idMaquina)) || `Máquina ID ${idMaquina}`;
        avgTemperatures[maquinaNome] = parseFloat((tempSum[idMaquina] / tempCount[idMaquina]).toFixed(2));
    }

    charts.temperaturaOleoChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Object.keys(avgTemperatures),
            datasets: [{
                label: 'Média de Temperatura do Óleo (°C)',
                data: Object.values(avgTemperatures),
                backgroundColor: 'rgba(255, 159, 64, 0.6)',
                borderColor: 'rgba(255, 159, 64, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Temperatura Média (°C)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Máquina'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: Object.keys(avgTemperatures).length === 0 ? 'Nenhum dado de temperatura para exibir' : 'Média de Temperatura do Óleo por Máquina'
                }
            }
        }
    });
}

function renderCiclosOperacaoChart(telemetrias, selectedMachineId) {
    const ctx = document.getElementById('ciclosOperacaoChart').getContext('2d');

    const today = new Date();
    today.setHours(0, 0, 0, 0); // Começo do dia
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1); // Fim do dia (começo do próximo)

    const ciclosDoDia = telemetrias.filter(t =>
        t.dataHoraColeta >= today && t.dataHoraColeta < tomorrow &&
        t.ciclosOperacao !== undefined && t.ciclosOperacao !== null
    );

    let labels = [];
    let data = [];
    let chartTitle = 'Ciclos de Operação do Dia';

    if (selectedMachineId !== null) {
        const machineTelemetriesToday = ciclosDoDia.filter(t => t.idMaquina === selectedMachineId);
        const totalCiclos = machineTelemetriesToday.reduce((sum, t) => sum + t.ciclosOperacao, 0);
        const maquinaNome = allMachines.find(m => m.id === selectedMachineId)?.nome || `Máquina ID ${selectedMachineId}`;
        labels.push(maquinaNome);
        data.push(totalCiclos);
        chartTitle = `Ciclos de Operação do Dia para ${maquinaNome}`;
    } else {
        const ciclosPorMaquinaHoje = ciclosDoDia.reduce((acc, t) => {
            const maquinaNome = allMachines.find(m => m.id === t.idMaquina)?.nome || `Máquina ID ${t.idMaquina}`;
            acc[maquinaNome] = (acc[maquinaNome] || 0) + t.ciclosOperacao;
            return acc;
        }, {});
        labels = Object.keys(ciclosPorMaquinaHoje);
        data = Object.values(ciclosPorMaquinaHoje);
    }

    charts.ciclosOperacaoChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Ciclos de Operação',
                data: data,
                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Total de Ciclos'
                    },
                    ticks: {
                        precision: 0
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: selectedMachineId === null ? 'Máquina' : ''
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: labels.length === 0 ? 'Nenhum dado de ciclos para exibir hoje' : chartTitle
                }
            }
        }
    });
}


// --- GRÁFICOS DE TELEMETRIA (Existentes) ---

function renderTelemetryTrendsChart(telemetrias) {
    const ctx = document.getElementById('telemetryTrendsChart').getContext('2d');

    const labels = telemetrias.map(t => t.dataHoraColeta); // Já são objetos Date
    const pressaoData = telemetrias.map(t => t.pressaoHidraulica);
    const temperaturaData = telemetrias.map(t => t.temperaturaOleo);
    const vibracaoData = telemetrias.map(t => t.vibracao);

    charts.telemetryTrendsChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Pressão Hidráulica (Bar)',
                    data: pressaoData,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    fill: false,
                    tension: 0.1
                },
                {
                    label: 'Temperatura do Óleo (°C)',
                    data: temperaturaData,
                    borderColor: 'rgba(255, 159, 64, 1)',
                    backgroundColor: 'rgba(255, 159, 64, 0.2)',
                    fill: false,
                    tension: 0.1
                },
                {
                    label: 'Vibração (Hz)',
                    data: vibracaoData,
                    borderColor: 'rgba(153, 102, 255, 1)',
                    backgroundColor: 'rgba(153, 102, 255, 0.2)',
                    fill: false,
                    tension: 0.1
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Valor'
                    }
                },
                x: {
                    type: 'time',
                    time: {
                        unit: 'hour', // Pode ajustar para 'minute', 'day' conforme a densidade dos dados
                        displayFormats: {
                            hour: 'HH:mm',
                            day: 'DD/MM'
                        }
                    },
                    title: {
                        display: true,
                        text: 'Data/Hora da Coleta'
                    }
                }
            },
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: telemetrias.length === 0 ? 'Nenhum dado de tendência para exibir' : 'Tendências de Telemetria ao Longo do Tempo'
                }
            }
        }
    });
}

function renderSensorNivelBaixoChart(telemetrias) {
    const ctx = document.getElementById('sensorNivelBaixoChart').getContext('2d');

    const sensorStatusCounts = {
        ativado: 0,
        desativado: 0
    };

    telemetrias.forEach(t => {
        if (t.sensorNivelBaixo) {
            sensorStatusCounts.ativado++;
        } else {
            sensorStatusCounts.desativado++;
        }
    });

    charts.sensorNivelBaixoChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Sensor Ativado (Nível Baixo)', 'Sensor Desativado (Nível OK)'],
            datasets: [{
                data: [sensorStatusCounts.ativado, sensorStatusCounts.desativado],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.6)', // Vermelho para ativado (problema)
                    'rgba(75, 192, 192, 0.6)'  // Verde/Azul para desativado (ok)
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(75, 192, 192, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: telemetrias.length === 0 ? 'Nenhum dado de sensor para exibir' : 'Distribuição de Eventos de Sensor de Nível Baixo'
                }
            }
        }
    });
}

function renderVibracaoMediaChart(telemetrias, maquinas) {
    const ctx = document.getElementById('vibracaoMediaChart').getContext('2d');
    const maquinasMap = new Map(maquinas.map(m => [m.id, m.nome]));

    const vibracaoSum = {};
    const vibracaoCount = {};

    telemetrias.forEach(t => {
        if (t.vibracao !== undefined && t.vibracao !== null) {
            vibracaoSum[t.idMaquina] = (vibracaoSum[t.idMaquina] || 0) + t.vibracao;
            vibracaoCount[t.idMaquina] = (vibracaoCount[t.idMaquina] || 0) + 1;
        }
    });

    const avgVibrations = {};
    for (const idMaquina in vibracaoSum) {
        const maquinaNome = maquinasMap.get(parseInt(idMaquina)) || `Máquina ID ${idMaquina}`;
        avgVibrations[maquinaNome] = parseFloat((vibracaoSum[idMaquina] / vibracaoCount[idMaquina]).toFixed(2));
    }

    charts.vibracaoMediaChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Object.keys(avgVibrations),
            datasets: [{
                label: 'Média de Vibração (Hz)',
                data: Object.values(avgVibrations),
                backgroundColor: 'rgba(255, 205, 86, 0.6)',
                borderColor: 'rgba(255, 205, 86, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Vibração Média (Hz)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Máquina'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: Object.keys(avgVibrations).length === 0 ? 'Nenhum dado de vibração para exibir' : 'Média de Vibração por Máquina'
                }
            }
        }
    });
}

// --- NOVOS GRÁFICOS E INFORMAÇÕES ADICIONAIS ---

function renderFalhasPorDataChart(historicoFalhas) {
    const ctx = document.getElementById('falhasPorDataChart').getContext('2d');

    // Agrupa falhas por data (dia)
    const falhasPorDia = historicoFalhas.reduce((acc, falha) => {
        const date = falha.dataFalha; // Já é um objeto Date
        const day = moment(date).format('YYYY-MM-DD'); // Usa Moment.js para formatar
        acc[day] = (acc[day] || 0) + 1;
        return acc;
    }, {});

    // Ordena as datas cronologicamente
    const sortedDates = Object.keys(falhasPorDia).sort((a, b) => new Date(a) - new Date(b));
    const dataCounts = sortedDates.map(date => falhasPorDia[date]);

    charts.falhasPorDataChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: sortedDates,
            datasets: [{
                label: 'Número de Falhas',
                data: dataCounts,
                borderColor: 'rgba(255, 99, 132, 1)',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                fill: true,
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Quantidade de Falhas'
                    },
                    ticks: {
                        precision: 0 // Garante que os ticks do eixo Y sejam inteiros
                    }
                },
                x: {
                    type: 'time', // Para exibir datas corretamente
                    time: {
                        unit: 'day', // Agrupa por dia
                        tooltipFormat: 'DD/MM/YYYY',
                        displayFormats: {
                            day: 'DD/MM'
                        }
                    },
                    title: {
                        display: true,
                        text: 'Data da Falha'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: historicoFalhas.length === 0 ? 'Nenhum dado de falhas por data para exibir' : 'Ocorrência de Falhas ao Longo do Tempo'
                }
            }
        }
    });
}


function renderTempoManutencaoPorMesChart(historicoManutencao) {
    const ctx = document.getElementById('tempoManutencaoPorMesChart').getContext('2d');

    const tempoPorMes = historicoManutencao.reduce((acc, manutencao) => {
        const date = manutencao.dataHoraManutencao; // Já é um objeto Date
        const yearMonth = moment(date).format('YYYY-MM'); // Usa Moment.js para formatar
        acc[yearMonth] = (acc[yearMonth] || 0) + manutencao.tempoManutencaoMin;
        return acc;
    }, {});

    const sortedMonths = Object.keys(tempoPorMes).sort();
    const dataCounts = sortedMonths.map(month => tempoPorMes[month]);

    charts.tempoManutencaoPorMesChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: sortedMonths,
            datasets: [{
                label: 'Tempo de Manutenção (Minutos)',
                data: dataCounts,
                backgroundColor: 'rgba(75, 192, 192, 0.6)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Tempo Total (Minutos)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Mês'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: historicoManutencao.length === 0 ? 'Nenhum dado de manutenção por mês para exibir' : 'Tempo de Manutenção por Mês'
                }
            }
        }
    });
}

function renderTop5FalhasChart(historicoFalhas) {
    // 1. Obtém o contexto do canvas para desenhar o gráfico
    const ctx = document.getElementById('top5FalhasChart').getContext('2d');

    // 2. Conta a ocorrência de cada tipo de falha
    const falhasCount = historicoFalhas.reduce((acc, falha) => {
        const tipoFalhaNormalizado = falha.tipoFalha.trim(); // Remove espaços extras para normalizar
        // CORREÇÃO: troquei 'tipoFalhoNormalizado' por 'tipoFalhaNormalizado'
        acc[tipoFalhaNormalizado] = (acc[tipoFalhaNormalizado] || 0) + 1;
        return acc;
    }, {});

    // 3. Ordena as falhas por ocorrência (do maior para o menor) e pega as Top 5
    const sortedFalhas = Object.entries(falhasCount)
        .sort(([,a],[,b]) => b - a)
        .slice(0, 5); // Pega apenas os 5 primeiros

    // 4. Prepara os rótulos (nomes das falhas) e os dados (quantidades) para o gráfico
    const labels = sortedFalhas.map(entry => entry[0]);
    const data = sortedFalhas.map(entry => entry[1]);

    // 5. Destroi o gráfico existente se houver (para evitar duplicatas ao atualizar)
    if (charts.top5FalhasChart) {
        charts.top5FalhasChart.destroy();
    }

    // 6. Cria e renderiza o novo gráfico de barras
    charts.top5FalhasChart = new Chart(ctx, {
        type: 'bar', // Tipo de gráfico: barras
        data: {
            labels: labels, // Rótulos no eixo X
            datasets: [{
                label: 'Número de Ocorrências', // Legenda do dataset
                data: data, // Dados para as barras
                backgroundColor: 'rgba(255, 99, 132, 0.6)', // Cor de preenchimento das barras
                borderColor: 'rgba(255, 99, 132, 1)', // Cor da borda das barras
                borderWidth: 1 // Largura da borda
            }]
        },
        options: {
            responsive: true, // Torna o gráfico responsivo
            maintainAspectRatio: false, // Permite que o gráfico não mantenha a proporção original
            scales: {
                y: {
                    beginAtZero: true, // Começa o eixo Y em zero
                    title: {
                        display: true,
                        text: 'Quantidade' // Título do eixo Y
                    },
                    ticks: {
                        precision: 0 // Mostra apenas números inteiros no eixo Y
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Tipo de Falha' // Título do eixo X
                    }
                }
            },
            plugins: {
                legend: {
                    display: false // Oculta a legenda do dataset
                },
                title: {
                    display: true,
                    // Título dinâmico: se não houver falhas, mostra uma mensagem, caso contrário, o título padrão
                    text: labels.length === 0 ? 'Nenhuma falha para exibir' : 'Principais Falhas das Máquinas (Top 5)'
                }
            }
        }
    });
}

