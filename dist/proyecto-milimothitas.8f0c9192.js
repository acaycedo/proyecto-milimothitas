// Variables globales
let currentPage = 'products';
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
axiosInstance.interceptors.request.use((request)=>{
    console.log("Iniciando petici\xf3n:", request);
    return request;
});
axiosInstance.interceptors.response.use((response)=>{
    console.log('Respuesta recibida:', response);
    return response;
}, (error)=>{
    console.error("Error en la petici\xf3n:", error);
    if (error.response) {
        console.error('Datos del error:', error.response.data);
        console.error('Estado del error:', error.response.status);
    }
    return Promise.reject(error);
});
// Utilidades
const showAlert = (message, type = 'success')=>{
    const alertContainer = document.getElementById('alert-container');
    if (!alertContainer) {
        console.error("No se encontr\xf3 el contenedor de alertas");
        return;
    }
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    alertContainer.appendChild(alert);
    setTimeout(()=>alert.remove(), 5000);
};
const showError = (message, error)=>{
    console.error('Error completo:', error);
    const alertContainer = document.getElementById('alert-container');
    if (!alertContainer) {
        console.error("No se encontr\xf3 el contenedor de alertas");
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
    setTimeout(()=>alertDiv.remove(), 5000);
};
const formatPrice = (price)=>{
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS'
    }).format(price);
};
// Función para formatear moneda en pesos colombianos
const formatCurrency = (amount)=>{
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(amount);
};
// Función para formatear precio en pesos colombianos
const formatPriceCOP = (price)=>{
    if (!price) return '$0';
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(price);
};
// Función para inicializar el modal
const initializeModal = ()=>{
    const modalElement = document.getElementById('formModal');
    if (!modalElement) {
        console.error("No se encontr\xf3 el elemento del modal");
        return false;
    }
    formModal = new bootstrap.Modal(modalElement);
    return true;
};
// Función para mostrar el modal
const showModal = (title, item = null)=>{
    if (!formModal) {
        if (!initializeModal()) {
            console.error('No se pudo inicializar el modal');
            return;
        }
    }
    currentItem = item;
    const modalTitle = document.getElementById('modalTitle');
    const productFields = document.getElementById('product-fields');
    const userFields = document.getElementById('user-fields');
    const form = document.getElementById('dataForm');
    if (modalTitle) modalTitle.textContent = title;
    if (form) form.reset();
    if (currentPage === 'products') {
        if (productFields) productFields.style.display = 'block';
        if (userFields) userFields.style.display = 'none';
        if (item) {
            document.getElementById('code').value = item.code || '';
            document.getElementById('description').value = item.description || '';
            document.getElementById('category').value = item.category || '';
            document.getElementById('price').value = item.price || '';
            document.getElementById('stock').value = item.stock || '';
        }
    } else {
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
const loadProducts = async ()=>{
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
const renderProducts = ()=>{
    const tbody = document.getElementById('products-table-body');
    if (!tbody) {
        console.error("No se encontr\xf3 la tabla de productos");
        return;
    }
    tbody.innerHTML = products.map((product)=>`
        <tr>
            <td>${product.id}</td>
            <td>${product.code || ''}</td>
            <td>${product.description || ''}</td>
            <td>${product.category || ''}</td>
            <td>${formatPriceCOP(product.price)}</td>
            <td>${product.stock || 0}</td>
            <td>${product.state ? 'Activo' : 'Inactivo'}</td>
            <td>
                <button class="btn btn-sm btn-warning" onclick="editProduct(${product.id})">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteProduct(${product.id})">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
};
// Funciones para usuarios
const loadUsers = async ()=>{
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
const renderUsers = ()=>{
    const tbody = document.getElementById('users-table-body');
    if (!tbody) {
        console.error("No se encontr\xf3 la tabla de usuarios");
        return;
    }
    tbody.innerHTML = users.map((user)=>`
        <tr>
            <td>${user.userId}</td>
            <td>${user.name || ''}</td>
            <td>${user.email || ''}</td>
            <td>${user.role || 'USER'}</td>
            <td>${user.state || 'Active'}</td>
            <td>
                <button class="btn btn-sm btn-warning" onclick="editUser(${user.userId})">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.userId})">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
};
// Funciones para editar
const editProduct = (id)=>{
    const product = products.find((p)=>p.id === id);
    if (product) showModal('Editar Producto', product);
};
const editUser = (id)=>{
    const user = users.find((u)=>u.userId === id);
    if (user) showModal('Editar Usuario', user);
};
// Funciones para eliminar
const deleteProduct = async (id)=>{
    if (!confirm("\xbfEst\xe1 seguro de eliminar este producto?")) return;
    try {
        await axiosInstance.delete(`/api/products/${id}`);
        showAlert('Producto eliminado exitosamente');
        await loadProducts();
    } catch (error) {
        showError('Error al eliminar producto', error);
    }
};
const deleteUser = async (id)=>{
    if (!confirm("\xbfEst\xe1 seguro de eliminar este usuario?")) return;
    try {
        await axiosInstance.delete(`/api/users/${id}`);
        showAlert('Usuario eliminado exitosamente');
        await loadUsers();
    } catch (error) {
        showError('Error al eliminar usuario', error);
    }
};
// Navegación
const showSection = (section)=>{
    currentPage = section;
    const sections = document.querySelectorAll('.section');
    sections.forEach((s)=>s.style.display = 'none');
    const activeSection = document.getElementById(`${section}-section`);
    if (activeSection) activeSection.style.display = 'block';
    // Actualizar navegación activa
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach((link)=>{
        if (link.getAttribute('data-section') === section) link.classList.add('active');
        else link.classList.remove('active');
    });
    // Cargar datos según la sección
    if (section === 'products') loadProducts();
    else if (section === 'users') loadUsers();
};
// Función para validar el formulario
const validateForm = ()=>{
    const form = document.getElementById('dataForm');
    if (!form) return false;
    if (currentPage === 'products') {
        const code = document.getElementById('code').value;
        const description = document.getElementById('description').value;
        const category = document.getElementById('category').value;
        const price = document.getElementById('price').value;
        const stock = document.getElementById('stock').value;
        if (!code || !description || !category || !price || !stock) {
            showError("Error de validaci\xf3n", 'Todos los campos son obligatorios');
            return false;
        }
        if (isNaN(price) || parseFloat(price) <= 0) {
            showError("Error de validaci\xf3n", "El precio debe ser un n\xfamero mayor a 0");
            return false;
        }
        if (isNaN(stock) || parseInt(stock) < 0) {
            showError("Error de validaci\xf3n", "El stock debe ser un n\xfamero mayor o igual a 0");
            return false;
        }
    } else {
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const role = document.getElementById('role').value;
        if (!name || !email || !role) {
            showError("Error de validaci\xf3n", 'Todos los campos son obligatorios');
            return false;
        }
        if (!currentItem && !document.getElementById('password').value) {
            showError("Error de validaci\xf3n", "La contrase\xf1a es obligatoria para nuevos usuarios");
            return false;
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            showError("Error de validaci\xf3n", "El formato del email no es v\xe1lido");
            return false;
        }
    }
    return true;
};
// Inicialización
document.addEventListener('DOMContentLoaded', ()=>{
    console.log("Inicializando aplicaci\xf3n...");
    // Inicializar el modal
    if (!initializeModal()) {
        console.error('Error al inicializar el modal');
        return;
    }
    // Configurar navegación
    const navLinks = document.querySelectorAll('.nav-link[data-section]');
    navLinks.forEach((link)=>{
        link.addEventListener('click', (e)=>{
            e.preventDefault();
            const section = e.target.getAttribute('data-section');
            showSection(section);
        });
    });
    // Configurar botón guardar
    const saveButton = document.getElementById('saveButton');
    const form = document.getElementById('dataForm');
    if (saveButton && form) saveButton.addEventListener('click', async ()=>{
        if (!validateForm()) return;
        let formData;
        try {
            if (currentPage === 'products') formData = {
                code: document.getElementById('code').value,
                description: document.getElementById('description').value,
                category: document.getElementById('category').value,
                price: parseFloat(document.getElementById('price').value),
                stock: parseInt(document.getElementById('stock').value),
                state: true
            };
            else {
                formData = {
                    name: document.getElementById('name').value,
                    email: document.getElementById('email').value,
                    role: document.getElementById('role').value,
                    state: 'Active'
                };
                const password = document.getElementById('password').value;
                if (password) formData.password = password;
            }
            if (currentItem) {
                // Actualizar
                if (currentPage === 'products') {
                    await axiosInstance.put(`/api/products/${currentItem.id}`, formData);
                    showAlert('Producto actualizado exitosamente');
                    await loadProducts();
                } else {
                    await axiosInstance.put(`/api/users/${currentItem.userId}`, formData);
                    showAlert('Usuario actualizado exitosamente');
                    await loadUsers();
                }
            } else // Crear
            if (currentPage === 'products') {
                await axiosInstance.post('/api/products', formData);
                showAlert('Producto creado exitosamente');
                await loadProducts();
            } else {
                await axiosInstance.post('/api/users', formData);
                showAlert('Usuario creado exitosamente');
                await loadUsers();
            }
            if (formModal) formModal.hide();
        } catch (error) {
            console.error('Error al guardar:', error);
            if (error.response) showError('Error al guardar', error.response.data || 'Error en el servidor');
            else showError('Error al guardar', "Error de conexi\xf3n con el servidor");
        }
    });
    // Cargar datos iniciales
    showSection('products');
});

//# sourceMappingURL=proyecto-milimothitas.8f0c9192.js.map
