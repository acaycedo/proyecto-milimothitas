// Variables globales
let currentPage = 'dashboard';
let products = [];
let users = [];
let currentItem = null;
let formModal = null;

// Configuración de axios
const API_URL = 'http://localhost:8080';

const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
    timeout: 10000
});

// Agregar interceptor para logs
axiosInstance.interceptors.request.use(request => {
    console.log('Iniciando petición:', request);
    return request;
});

axiosInstance.interceptors.response.use(
    response => {
        console.log('Respuesta recibida:', response);
        return response;
    },
    error => {
        console.error('Error en la petición:', error);
        if (error.response) {
            console.error('Datos del error:', error.response.data);
            console.error('Estado del error:', error.response.status);
        }
        return Promise.reject(error);
    }
);

// Utilidades
const showAlert = (message, type = 'success') => {
    const alertContainer = document.getElementById('alert-container');
    if (!alertContainer) {
        console.error('No se encontró el contenedor de alertas');
        return;
    }
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    alertContainer.appendChild(alert);
    setTimeout(() => alert.remove(), 5000);
};

const showError = (message, error) => {
    console.error('Error completo:', error);
    const alertContainer = document.getElementById('alert-container');
    if (!alertContainer) {
        console.error('No se encontró el contenedor de alertas');
        return;
    }
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-danger alert-dismissible fade show';
    alertDiv.innerHTML = `
        <strong>Error:</strong> ${message}
        <br>
        <small>${error.response?.data?.message || error.message}</small>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    alertContainer.appendChild(alertDiv);
    setTimeout(() => alertDiv.remove(), 5000);
};

const formatPrice = (price) => {
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS'
    }).format(price);
};

// Función para formatear moneda en pesos colombianos
const formatCurrency = (amount) => {
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(amount);
};

// Función para formatear fechas en formato colombiano
const formatDate = (dateString) => {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('es-CO', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    }).format(date);
};

// Función para formatear precio en pesos colombianos
const formatPriceCOP = (price) => {
    if (!price) return '$0';
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(price);
};

// Función para inicializar el modal
const initializeModal = () => {
    const modalElement = document.getElementById('formModal');
    if (!modalElement) {
        console.error('No se encontró el elemento del modal');
        return false;
    }
    formModal = new bootstrap.Modal(modalElement);
    return true;
};

// Función para mostrar el modal
const showModal = (title, item = null) => {
    if (!formModal) {
        if (!initializeModal()) {
            console.error('No se pudo inicializar el modal');
            return;
        }
    }
    currentItem = item;
    const modalTitle = document.getElementById('modalTitle');
    const saveButton = document.getElementById('saveButton');
    const productFields = document.getElementById('product-fields');
    const userFields = document.getElementById('user-fields');
    const form = document.getElementById('dataForm');
    if (form) form.reset();
    if (currentPage === 'catalogo') {
        if (modalTitle) modalTitle.textContent = title.includes('Editar') ? 'Editar Producto' : 'Nuevo Producto';
        if (saveButton) saveButton.textContent = item ? 'Actualizar Producto' : 'Guardar Producto';
        if (productFields) productFields.style.display = 'block';
        if (userFields) userFields.style.display = 'none';
        if (item) {
            document.getElementById('code').value = item.code || '';
            document.getElementById('description').value = item.description || '';
            document.getElementById('category').value = item.category || '';
            document.getElementById('price').value = item.price || '';
            document.getElementById('stock').value = item.stock || '';
        }
    } else if (currentPage === 'embajadoras') {
        if (modalTitle) modalTitle.textContent = title.includes('Editar') ? 'Editar Embajadora' : 'Nueva Embajadora';
        if (saveButton) saveButton.textContent = item ? 'Actualizar Embajadora' : 'Guardar Embajadora';
        if (productFields) productFields.style.display = 'none';
        if (userFields) userFields.style.display = 'block';
        if (item) {
            document.getElementById('name').value = item.name || '';
            document.getElementById('email').value = item.email || '';
            document.getElementById('role').value = item.role || 'USER';
            document.getElementById('password').value = '';
        }
    }
    formModal.show();
};

// Funciones para productos
const loadProducts = async () => {
    try {
        console.log('Cargando productos...');
        const response = await axiosInstance.get('/api/products');
        console.log('Productos cargados:', response.data);
        products = response.data;
        renderProducts();
    } catch (error) {
        console.error('Error al cargar productos:', error);
        showError('Error al cargar productos', error);
    }
};

const renderProducts = () => {
    const tbody = document.getElementById('products-table-body');
    if (!tbody) {
        console.error('No se encontró la tabla de productos');
        return;
    }
    tbody.innerHTML = products.map(product => `
        <tr>
            <td>${product.category || ''}</td>
            <td>${product.code || ''}</td>
            <td>${product.description || ''}</td>
            <td>${product.stock || 0}</td>
            <td>${formatPriceCOP(product.price)}</td>
            <td>${product.state ? 'Activo' : 'Inactivo'}</td>
            <td>
                <button class="btn btn-sm btn-outline-primary" onclick="editProduct(${product.id})">
                    <i class="bi bi-pencil-square"></i>
                </button>
            </td>
            <td>
                <button class="btn btn-sm btn-secondary" onclick="deleteProduct(${product.id})">
                    <i class="bi bi-x"></i> Cancelar
                </button>
            </td>
        </tr>
    `).join('');
};

// Funciones para usuarios
const loadUsers = async () => {
    try {
        console.log('Cargando usuarios...');
        const response = await axiosInstance.get('/api/users');
        console.log('Usuarios cargados:', response.data);
        users = response.data;
        renderUsers();
    } catch (error) {
        console.error('Error al cargar usuarios:', error);
        showError('Error al cargar usuarios', error);
    }
};

const renderUsers = () => {
    const tbody = document.getElementById('users-table-body');
    if (!tbody) {
        console.error('No se encontró la tabla de usuarios');
        return;
    }
    tbody.innerHTML = users.map(user => `
        <tr>
            <td>${user.userId}</td>
            <td>${user.name || ''}</td>
            <td>${user.email || ''}</td>
            <td>${user.role || 'USER'}</td>
            <td>${user.state || 'Active'}</td>
            <td>
                <button class="btn btn-sm btn-outline-primary" onclick="editUser(${user.userId})">
                    <i class="bi bi-pencil-square"></i>
                </button>
            </td>
            <td>
                <button class="btn btn-sm btn-secondary" onclick="deleteUser(${user.userId})">
                    <i class="bi bi-x"></i> Eliminar
                </button>
            </td>
        </tr>
    `).join('');
};

// Funciones para editar
const editProduct = (id) => {
    const product = products.find(p => p.id === id);
    if (product) {
        showModal('Editar Producto', product);
    }
};

const editUser = (id) => {
    const user = users.find(u => u.userId === id);
    if (user) {
        showModal('Editar Usuario', user);
    }
};

// Funciones para eliminar
const deleteProduct = async (id) => {
    if (!confirm('¿Está seguro de eliminar este producto?')) return;
    try {
        await axiosInstance.delete(`/api/products/${id}`);
        showAlert('Producto eliminado exitosamente');
        await loadProducts();
    } catch (error) {
        showError('Error al eliminar producto', error);
    }
};

const deleteUser = async (id) => {
    if (!confirm('¿Está seguro de eliminar este usuario?')) return;
    try {
        await axiosInstance.delete(`/api/users/${id}`);
        showAlert('Usuario eliminado exitosamente');
        await loadUsers();
    } catch (error) {
        showError('Error al eliminar usuario', error);
    }
};

// Navegación
const showSection = (section) => {
    currentPage = section;
    const sections = document.querySelectorAll('.section');
    sections.forEach(s => s.style.display = 'none');
    const activeSection = document.getElementById(`${section}-section`);
    if (activeSection) {
        activeSection.style.display = 'block';
    }
    // Actualizar navegación activa
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        if (link.getAttribute('data-section') === section) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
    // Cargar datos según la sección
    if (section === 'catalogo') {
        loadProducts();
    } else if (section === 'embajadoras') {
        loadUsers();
    } else if (section === 'reportes') {
        // Asumiendo que cargarVentas es una función global accesible o definida en sales.js y cargada antes.
        if (typeof cargarVentas === 'function') {
            cargarVentas();
        } else {
            console.error('Error: cargarVentas() no está definida.');
        }
    } else if (section === 'dashboard') {
        loadSalesSummaryByMonth();
    }
};

// Función para validar el formulario
const validateForm = () => {
    const form = document.getElementById('dataForm');
    if (!form) return false;

    if (currentPage === 'catalogo') {
        const code = document.getElementById('code').value;
        const description = document.getElementById('description').value;
        const category = document.getElementById('category').value;
        const price = document.getElementById('price').value;
        const stock = document.getElementById('stock').value;

        if (!code || !description || !category || !price || !stock) {
            showError('Error de validación', 'Todos los campos son obligatorios');
            return false;
        }

        if (isNaN(price) || parseFloat(price) <= 0) {
            showError('Error de validación', 'El precio debe ser un número mayor a 0');
            return false;
        }

        if (isNaN(stock) || parseInt(stock) < 0) {
            showError('Error de validación', 'El stock debe ser un número mayor o igual a 0');
            return false;
        }
    } else {
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const role = document.getElementById('role').value;

        if (!name || !email || !role) {
            showError('Error de validación', 'Todos los campos son obligatorios');
            return false;
        }

        if (!currentItem && !document.getElementById('password').value) {
            showError('Error de validación', 'La contraseña es obligatoria para nuevos usuarios');
            return false;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            showError('Error de validación', 'El formato del email no es válido');
            return false;
        }
    }

    return true;
};

// Funciones de búsqueda para productos
const searchProductsByCode = async (code) => {
    try {
        const response = await axiosInstance.get(`/api/products/search?codigo=${encodeURIComponent(code)}`);
        products = response.data;
        renderProducts();
    } catch (error) {
        showError('Error al buscar productos por código', error);
    }
};

const searchProductsByName = async (name) => {
    try {
        const response = await axiosInstance.get(`/api/products/search?nombre=${encodeURIComponent(name)}`);
        products = response.data;
        renderProducts();
    } catch (error) {
        showError('Error al buscar productos por nombre', error);
    }
};

const searchProductsByCategory = async (category) => {
    try {
        const response = await axiosInstance.get(`/api/products/search?categoria=${encodeURIComponent(category)}`);
        products = response.data;
        renderProducts();
    } catch (error) {
        showError('Error al buscar productos por categoría', error);
    }
};

// Funciones de búsqueda para usuarios
const searchUsersByName = async (name) => {
    try {
        const response = await axiosInstance.get(`/api/users/search?nombre=${encodeURIComponent(name)}`);
        users = response.data;
        renderUsers();
    } catch (error) {
        showError('Error al buscar usuarios por nombre', error);
    }
};

const searchUsersByEmail = async (email) => {
    try {
        const response = await axiosInstance.get(`/api/users/search?email=${encodeURIComponent(email)}`);
        users = response.data;
        renderUsers();
    } catch (error) {
        showError('Error al buscar usuarios por email', error);
    }
};

// Event Listeners para los campos de búsqueda
document.addEventListener('DOMContentLoaded', () => {
    console.log('Inicializando aplicación...');
    
    // Inicializar el modal
    if (!initializeModal()) {
        console.error('Error al inicializar el modal');
        return;
    }
    
    // Configurar navegación
    const navLinks = document.querySelectorAll('.nav-link[data-section]');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const section = e.target.getAttribute('data-section');
            showSection(section);
        });
    });

    // Mostrar dashboard por defecto
    showSection('dashboard');

    // Configurar botón guardar
    const saveButton = document.getElementById('saveButton');
    const form = document.getElementById('dataForm');
    
    if (saveButton && form) {
        saveButton.addEventListener('click', async () => {
            if (!validateForm()) {
                return;
            }

            let formData;
            try {
                if (currentPage === 'catalogo') {
                    formData = {
                        code: document.getElementById('code').value,
                        description: document.getElementById('description').value,
                        category: document.getElementById('category').value,
                        price: parseFloat(document.getElementById('price').value),
                        stock: parseInt(document.getElementById('stock').value),
                        state: true
                    };
                } else {
                    formData = {
                        name: document.getElementById('name').value,
                        email: document.getElementById('email').value,
                        role: document.getElementById('role').value,
                        state: 'Active'
                    };
                    const password = document.getElementById('password').value;
                    if (password) {
                        formData.password = password;
                    }
                }

                if (currentItem) {
                    // Actualizar
                    if (currentPage === 'catalogo') {
                        await axiosInstance.put(`/api/products/${currentItem.id}`, formData);
                        showAlert('Producto actualizado exitosamente');
                        await loadProducts();
                    } else {
                        await axiosInstance.put(`/api/users/${currentItem.userId}`, formData);
                        showAlert('Usuario actualizado exitosamente');
                        await loadUsers();
                    }
                } else {
                    // Crear
                    if (currentPage === 'catalogo') {
                        await axiosInstance.post('/api/products', formData);
                        showAlert('Producto creado exitosamente');
                        await loadProducts();
                    } else {
                        await axiosInstance.post('/api/users', formData);
                        showAlert('Usuario creado exitosamente');
                        await loadUsers();
                    }
                }
                if (formModal) {
                    formModal.hide();
                }
            } catch (error) {
                console.error('Error al guardar:', error);
                if (error.response) {
                    showError('Error al guardar', error.response.data || 'Error en el servidor');
                } else {
                    showError('Error al guardar', 'Error de conexión con el servidor');
                }
            }
        });
    }

    // Cargar datos iniciales
    showSection('catalogo');

    // Búsqueda de productos
    const productSearchInput = document.getElementById('product-search');
    if (productSearchInput) {
        productSearchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.trim();
            if (searchTerm) {
                searchProductsByName(searchTerm);
            } else {
                loadProducts();
            }
        });
    }

    const productCodeSearchInput = document.getElementById('product-code-search');
    if (productCodeSearchInput) {
        productCodeSearchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.trim();
            if (searchTerm) {
                searchProductsByCode(searchTerm);
            } else {
                loadProducts();
            }
        });
    }

    const productCategorySearchInput = document.getElementById('product-category-search');
    if (productCategorySearchInput) {
        productCategorySearchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.trim();
            if (searchTerm) {
                searchProductsByCategory(searchTerm);
            } else {
                loadProducts();
            }
        });
    }

    // Búsqueda de usuarios
    const userSearchInput = document.getElementById('user-search');
    if (userSearchInput) {
        userSearchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.trim();
            if (searchTerm) {
                searchUsersByName(searchTerm);
            } else {
                loadUsers();
            }
        });
    }

    const userEmailSearchInput = document.getElementById('user-email-search');
    if (userEmailSearchInput) {
        userEmailSearchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.trim();
            if (searchTerm) {
                searchUsersByEmail(searchTerm);
            } else {
                loadUsers();
            }
        });
    }

    // Botón Registrar Producto en Catálogo
    const registerProductBtn = document.getElementById('register-product-btn');
    if (registerProductBtn) {
        registerProductBtn.addEventListener('click', () => {
            if (currentPage === 'catalogo') {
                showModal('Nuevo Producto');
            }
        });
    }

    // Botón Registrar Embajadora en Embajadoras
    const registerUserBtn = document.getElementById('register-user-btn');
    if (registerUserBtn) {
        registerUserBtn.addEventListener('click', () => {
            if (currentPage === 'embajadoras') {
                showModal('Nueva Embajadora');
            }
        });
    }
});

// Función para cargar el resumen de ventas por mes
const loadSalesSummaryByMonth = async () => {
    try {
        console.log('Cargando resumen de ventas por mes...');
        const response = await axiosInstance.get('/api/sales/summary-by-month');
        const salesSummary = response.data;
        console.log('Resumen de ventas cargado:', salesSummary);
        renderSalesSummaryByMonth(salesSummary);
        drawSalesCharts(salesSummary);
    } catch (error) {
        console.error('Error al cargar el resumen de ventas por mes:', error);
        showError('Error al cargar el resumen de ventas mensuales', error);
    }
};

// Función para renderizar el resumen de ventas por mes
const renderSalesSummaryByMonth = (summary) => {
    const tbody = document.getElementById('sales-summary-by-month-table-body');
    if (!tbody) {
        console.error('No se encontró la tabla de resumen de ventas por mes');
        return;
    }
    tbody.innerHTML = ''; // Limpiar tabla
    for (const [monthYear, totalSales] of Object.entries(summary)) {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${monthYear}</td>
            <td>${formatCurrency(totalSales)}</td>
        `;
        tbody.appendChild(row);
    }
};

// Función para dibujar los gráficos
let salesBarChartInstance = null;
let salesPieChartInstance = null;

const drawSalesCharts = (summary) => {
    const months = Object.keys(summary);
    const totals = Object.values(summary);

    // Gráfico de Barras
    const barCtx = document.getElementById('salesBarChart');
    if (barCtx) {
        if (salesBarChartInstance) {
            salesBarChartInstance.destroy(); // Destruir instancia anterior si existe
        }
        salesBarChartInstance = new Chart(barCtx, {
            type: 'bar',
            data: {
                labels: months,
                datasets: [{
                    label: 'Total de Ventas por Mes',
                    data: totals,
                    backgroundColor: 'rgba(136, 97, 123, 0.7)',
                    borderColor: 'rgba(136, 97, 123, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return formatCurrency(value);
                            }
                        }
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.dataset.label + ': ' + formatCurrency(context.raw);
                            }
                        }
                    }
                }
            }
        });
    } else {
        console.error('No se encontró el elemento canvas para el gráfico de barras.');
    }

    // Gráfico Circular de Ventas por Mes
    const salesPieChartCtx = document.getElementById('salesPieChart').getContext('2d');
    if (window.salesPieChart instanceof Chart) {
        window.salesPieChart.destroy();
    }
    window.salesPieChart = new Chart(salesPieChartCtx, {
        type: 'pie',
        data: {
            labels: months,
            datasets: [{
                data: totals,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.7)',
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(153, 102, 255, 0.7)',
                    'rgba(255, 159, 64, 0.7)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
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
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.parsed !== null) {
                                label += formatCurrency(context.parsed);
                            }
                            return label;
                        }
                    }
                }
            },
            layout: {
                padding: {
                    left: 10,
                    right: 10,
                    top: 10,
                    bottom: 10
                }
            }
        }
    });
}; 