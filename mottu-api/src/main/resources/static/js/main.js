/**
 * MOTTU - Sistema de Gerenciamento de Motos
 * Arquivo JavaScript Principal
 */

// Inicialização quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

/**
 * Inicializa a aplicação
 */
function initializeApp() {
    setupEventListeners();
    setupFormValidation();
    setupTableFeatures();
    setupModals();
    setupToasts();
    loadDashboardData();
}

/**
 * Configura os event listeners globais
 */
function setupEventListeners() {
    // Botões de confirmação de delete
    const deleteButtons = document.querySelectorAll('[data-confirm-delete]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', handleDeleteConfirmation);
    });

    // Filtros de pesquisa
    const searchInputs = document.querySelectorAll('[data-search]');
    searchInputs.forEach(input => {
        input.addEventListener('input', debounce(handleSearch, 300));
    });

    // Botões de atualização
    const refreshButtons = document.querySelectorAll('[data-refresh]');
    refreshButtons.forEach(button => {
        button.addEventListener('click', handleRefresh);
    });

    // Sidebar toggle para mobile
    const sidebarToggle = document.querySelector('#sidebar-toggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', toggleSidebar);
    }
}

/**
 * Configura validação de formulários
 */
function setupFormValidation() {
    const forms = document.querySelectorAll('form[data-validate]');
    forms.forEach(form => {
        form.addEventListener('submit', handleFormSubmit);
        
        // Validação em tempo real
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            input.addEventListener('blur', validateField);
            input.addEventListener('input', clearFieldError);
        });
    });
}

/**
 * Configura recursos de tabela
 */
function setupTableFeatures() {
    // Ordenação de colunas
    const sortableHeaders = document.querySelectorAll('th[data-sort]');
    sortableHeaders.forEach(header => {
        header.addEventListener('click', handleTableSort);
        header.style.cursor = 'pointer';
    });

    // Seleção múltipla
    const selectAllCheckbox = document.querySelector('#select-all');
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', handleSelectAll);
    }

    const rowCheckboxes = document.querySelectorAll('.row-checkbox');
    rowCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', handleRowSelection);
    });
}

/**
 * Configura modais
 */
function setupModals() {
    const modalTriggers = document.querySelectorAll('[data-modal]');
    modalTriggers.forEach(trigger => {
        trigger.addEventListener('click', openModal);
    });

    const modalCloses = document.querySelectorAll('.modal-close');
    modalCloses.forEach(close => {
        close.addEventListener('click', closeModal);
    });

    // Fechar modal clicando fora
    window.addEventListener('click', function(e) {
        if (e.target.classList.contains('modal')) {
            closeModal();
        }
    });
}

/**
 * Configura sistema de toast/notificações
 */
function setupToasts() {
    // Auto-hide toasts após 5 segundos
    const toasts = document.querySelectorAll('.toast');
    toasts.forEach(toast => {
        setTimeout(() => {
            hideToast(toast);
        }, 5000);
    });
}

/**
 * Carrega dados do dashboard via AJAX
 */
function loadDashboardData() {
    if (document.querySelector('.dashboard')) {
        fetchDashboardStats();
        setInterval(fetchDashboardStats, 30000); // Atualiza a cada 30 segundos
    }
}

/**
 * Busca estatísticas do dashboard
 */
async function fetchDashboardStats() {
    try {
        const response = await fetch('/api/dashboard/estatisticas');
        const data = await response.json();
        updateDashboardStats(data);
    } catch (error) {
        console.error('Erro ao carregar estatísticas:', error);
    }
}

/**
 * Atualiza as estatísticas no dashboard
 */
function updateDashboardStats(data) {
    const statsElements = {
        'total-motos': data.totalMotos,
        'motos-disponiveis': data.motosDisponiveis,
        'motos-alugadas': data.motosAlugadas,
        'motos-manutencao': data.motosManutencao,
        'alertas-hoje': data.alertasHoje,
        'sensores-bateria-baixa': data.sensoresBateriaBaixa
    };

    Object.entries(statsElements).forEach(([id, value]) => {
        const element = document.getElementById(id);
        if (element) {
            animateNumber(element, parseInt(element.textContent) || 0, value);
        }
    });
}

/**
 * Anima mudança de números
 */
function animateNumber(element, start, end) {
    const duration = 1000;
    const startTime = performance.now();
    
    function update(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        const current = Math.floor(start + (end - start) * progress);
        element.textContent = current;
        
        if (progress < 1) {
            requestAnimationFrame(update);
        }
    }
    
    requestAnimationFrame(update);
}

/**
 * Manipula confirmação de delete
 */
function handleDeleteConfirmation(e) {
    e.preventDefault();
    const message = e.target.dataset.confirmDelete || 'Tem certeza que deseja excluir este item?';
    
    if (confirm(message)) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = e.target.href;
        
        const methodInput = document.createElement('input');
        methodInput.type = 'hidden';
        methodInput.name = '_method';
        methodInput.value = 'DELETE';
        
        form.appendChild(methodInput);
        document.body.appendChild(form);
        form.submit();
    }
}

/**
 * Manipula busca com debounce
 */
function handleSearch(e) {
    const query = e.target.value;
    const searchType = e.target.dataset.search;
    
    if (query.length >= 2 || query.length === 0) {
        performSearch(searchType, query);
    }
}

/**
 * Executa busca
 */
async function performSearch(type, query) {
    const resultsContainer = document.querySelector(`#${type}-results`);
    if (!resultsContainer) return;
    
    showLoading(resultsContainer);
    
    try {
        const response = await fetch(`/api/${type}/search?q=${encodeURIComponent(query)}`);
        const data = await response.json();
        displaySearchResults(resultsContainer, data);
    } catch (error) {
        console.error('Erro na busca:', error);
        showError(resultsContainer, 'Erro ao realizar busca');
    }
}

/**
 * Manipula refresh de dados
 */
function handleRefresh(e) {
    e.preventDefault();
    const target = e.target.dataset.refresh;
    
    if (target === 'dashboard') {
        fetchDashboardStats();
        showToast('Dados atualizados com sucesso!', 'success');
    }
}

/**
 * Toggle do sidebar para mobile
 */
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    sidebar.classList.toggle('active');
}

/**
 * Manipula submit de formulário
 */
function handleFormSubmit(e) {
    const form = e.target;
    const isValid = validateForm(form);
    
    if (!isValid) {
        e.preventDefault();
        return false;
    }
    
    // Mostra loading
    const submitButton = form.querySelector('button[type="submit"]');
    if (submitButton) {
        submitButton.disabled = true;
        submitButton.innerHTML = '<span class="spinner"></span> Processando...';
    }
}

/**
 * Valida formulário completo
 */
function validateForm(form) {
    const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!validateField({ target: input })) {
            isValid = false;
        }
    });
    
    return isValid;
}

/**
 * Valida campo individual
 */
function validateField(e) {
    const field = e.target;
    const value = field.value.trim();
    let isValid = true;
    let errorMessage = '';
    
    // Validações básicas
    if (field.hasAttribute('required') && !value) {
        isValid = false;
        errorMessage = 'Este campo é obrigatório';
    } else if (field.type === 'email' && value && !isValidEmail(value)) {
        isValid = false;
        errorMessage = 'Email inválido';
    } else if (field.dataset.minLength && value.length < parseInt(field.dataset.minLength)) {
        isValid = false;
        errorMessage = `Mínimo de ${field.dataset.minLength} caracteres`;
    }
    
    // Validações específicas
    if (field.name === 'placa' && value && !isValidPlaca(value)) {
        isValid = false;
        errorMessage = 'Placa deve seguir o padrão brasileiro (ABC1234)';
    }
    
    if (isValid) {
        clearFieldError(field);
    } else {
        showFieldError(field, errorMessage);
    }
    
    return isValid;
}

/**
 * Limpa erro do campo
 */
function clearFieldError(field) {
    field.classList.remove('error');
    const errorElement = field.parentNode.querySelector('.field-error');
    if (errorElement) {
        errorElement.remove();
    }
}

/**
 * Mostra erro no campo
 */
function showFieldError(field, message) {
    field.classList.add('error');
    
    let errorElement = field.parentNode.querySelector('.field-error');
    if (!errorElement) {
        errorElement = document.createElement('div');
        errorElement.className = 'field-error';
        field.parentNode.appendChild(errorElement);
    }
    
    errorElement.textContent = message;
}

/**
 * Abre modal
 */
function openModal(e) {
    e.preventDefault();
    const modalId = e.target.dataset.modal;
    const modal = document.getElementById(modalId);
    
    if (modal) {
        modal.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }
}

/**
 * Fecha modal
 */
function closeModal() {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        modal.style.display = 'none';
    });
    document.body.style.overflow = 'auto';
}

/**
 * Mostra toast/notificação
 */
function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.classList.add('show');
    }, 100);
    
    setTimeout(() => {
        hideToast(toast);
    }, 5000);
}

/**
 * Esconde toast
 */
function hideToast(toast) {
    toast.classList.remove('show');
    setTimeout(() => {
        if (toast.parentNode) {
            toast.parentNode.removeChild(toast);
        }
    }, 300);
}

/**
 * Mostra loading
 */
function showLoading(container) {
    container.innerHTML = '<div class="spinner"></div>';
}

/**
 * Mostra erro
 */
function showError(container, message) {
    container.innerHTML = `<div class="alert alert-danger">${message}</div>`;
}

/**
 * Utilitário de debounce
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Validação de email
 */
function isValidEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

/**
 * Validação de placa brasileira
 */
function isValidPlaca(placa) {
    const re = /^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/;
    return re.test(placa.toUpperCase());
}

/**
 * Formata placa para exibição
 */
function formatPlaca(placa) {
    if (!placa) return '';
    const clean = placa.replace(/[^A-Z0-9]/g, '').toUpperCase();
    if (clean.length === 7) {
        return `${clean.slice(0, 3)}-${clean.slice(3)}`;
    }
    return clean;
}

/**
 * Formata valor monetário
 */
function formatCurrency(value) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(value);
}

/**
 * Formata data para exibição
 */
function formatDate(date) {
    return new Intl.DateTimeFormat('pt-BR').format(new Date(date));
}

/**
 * Formata data e hora para exibição
 */
function formatDateTime(datetime) {
    return new Intl.DateTimeFormat('pt-BR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    }).format(new Date(datetime));
}

// Exportar funções para uso global
window.MottuApp = {
    showToast,
    hideToast,
    openModal,
    closeModal,
    formatPlaca,
    formatCurrency,
    formatDate,
    formatDateTime,
    isValidEmail,
    isValidPlaca
};