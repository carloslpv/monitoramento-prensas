// js/dashboard.js

document.addEventListener('DOMContentLoaded', async () => {
    // Carregar todos os dados necessários para o dashboard
    try {
        // Para o dashboard funcionar, seus controllers precisam ter endpoints para listar todos os itens.
        // Vou assumir os seguintes endpoints de listagem:
        // GET /historicoFalhas/all
        // GET /historico-manutencao/all
        // GET /maquina/all
        // GET /telemetria/all

        const [maquinas, historicoFalhas, historicoManutencao, telemetrias] = await Promise.all([
            fetchData('/maquina/all'),
            fetchData('/historicoFalhas/all'),
            fetchData('/historico-manutencao/all'),
            fetchData('/telemetria/all')
        ]);

        renderTotalMaquinas(maquinas);
        renderFalhasPorTipoChart(historicoFalhas);
        renderTempoManutencaoChart(historicoManutencao, maquinas);
        renderMaquinasStatusChart(telemetrias);
        renderTemperaturaOleoChart(telemetrias, maquinas);
        renderCiclosOperacaoChart(telemetrias, maquinas);

    } catch (error) {
        console.error('Erro ao carregar dados para o dashboard:', error);
        displayMessage(`Erro ao carregar dados para o dashboard: ${error.message}`, 'error');
    }
});

function renderTotalMaquinas(maquinas) {
    const totalMaquinasElement = document.getElementById('total-maquinas');
    if (totalMaquinasElement) {
        totalMaquinasElement.textContent = maquinas.length;
    }
}

function renderFalhasPorTipoChart(historicoFalhas) {
    const ctx = document.getElementById('falhasPorTipoChart').getContext('2d');
    const falhasPorTipo = historicoFalhas.reduce((acc, falha) => {
        acc[falha.tipoFalha] = (acc[falha.tipoFalha] || 0) + 1;
        return acc;
    }, {});

    new Chart(ctx, {
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

    new Chart(ctx, {
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
                    'rgba(201, 203, 207, 0.6)'
                ],
                borderColor: [
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(201, 203, 207, 1)'
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
                    text: 'Tempo Total de Manutenção por Máquina (Minutos)'
                }
            }
        }
    });
}

function renderMaquinasStatusChart(telemetrias) {
    const ctx = document.getElementById('maquinasStatusChart').getContext('2d');

    // Mapeia o status mais recente de cada máquina
    const latestStatus = {};
    telemetrias.forEach(t => {
        if (!latestStatus[t.idMaquina] || new Date(t.dataHoraColeta) > new Date(latestStatus[t.idMaquina].dataHoraColeta)) {
            latestStatus[t.idMaquina] = t;
        }
    });

    const statusCounts = {
        ligadas: 0,
        desligadas: 0
    };

    Object.values(latestStatus).forEach(telemetria => {
        if (telemetria.maquinaLigada) {
            statusCounts.ligadas++;
        } else {
            statusCounts.desligadas++;
        }
    });

    new Chart(ctx, {
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
                    text: 'Status Atual das Máquinas'
                }
            }
        }
    });
}

function renderTemperaturaOleoChart(telemetrias, maquinas) {
    const ctx = document.getElementById('temperaturaOleoChart').getContext('2d');
    const maquinasMap = new Map(maquinas.map(m => [m.id, m.nome]));

    // Calcula a média de temperatura por máquina
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
        avgTemperatures[maquinaNome] = tempSum[idMaquina] / tempCount[idMaquina];
    }

    new Chart(ctx, {
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
                }
            }
        }
    });
}

function renderCiclosOperacaoChart(telemetrias, maquinas) {
    const ctx = document.getElementById('ciclosOperacaoChart').getContext('2d');
    const maquinasMap = new Map(maquinas.map(m => [m.id, m.nome]));

    // Sumariza ciclos de operação por máquina
    const ciclosPorMaquina = telemetrias.reduce((acc, t) => {
        if (t.ciclosOperacao !== undefined && t.ciclosOperacao !== null) {
            const maquinaNome = maquinasMap.get(t.idMaquina) || `Máquina ID ${t.idMaquina}`;
            acc[maquinaNome] = (acc[maquinaNome] || 0) + t.ciclosOperacao;
        }
        return acc;
    }, {});

    // Ordena e pega os top 5
    const sortedCiclos = Object.entries(ciclosPorMaquina)
        .sort(([,a],[,b]) => b - a)
        .slice(0, 5);

    const labels = sortedCiclos.map(entry => entry[0]);
    const data = sortedCiclos.map(entry => entry[1]);

    new Chart(ctx, {
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
                }
            }
        }
    });
}