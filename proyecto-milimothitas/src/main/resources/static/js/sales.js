console.log('sales.js cargado y ejecutándose');
// Variables globales
let currentSale = {
    saleItems: [],
    descuento: 0
};

// Funciones de cálculo
const calculateSaleTotals = () => {
    let subtotal = 0;
    if (currentSale.saleItems && currentSale.saleItems.length > 0) {
        subtotal = currentSale.saleItems.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    }
    const iva = subtotal * 0.19; // 19% IVA
    const total = subtotal + iva - (currentSale.descuento || 0);

    document.getElementById('current-sale-total').textContent = formatCurrency(total);
};

// Funciones de UI para la sección de ventas principal
const renderCurrentSaleProductsTable = () => {
    const tbody = document.getElementById('current-sale-products-table-body');
    tbody.innerHTML = '';

    currentSale.saleItems.forEach(item => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${item.product.code}</td>
            <td>${item.product.description}</td>
            <td>${formatCurrency(item.product.price)}</td>
            <td>${item.quantity}</td>
            <td>
                <button type="button" class="btn btn-sm btn-danger" onclick="removeProductFromCurrentSale(${item.product.id})">
                    <i class="bi bi-trash"></i> Eliminar
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });
    calculateSaleTotals();
};

const addProductToCurrentSale = async () => {
    const productSearchInput = document.getElementById('sale-product-search-input');
    const quantityInput = document.getElementById('quantity-input');

    const query = productSearchInput.value.trim();
    const quantity = parseInt(quantityInput.value) || 1;

    console.log('Intentando añadir producto. Query:', query, 'Cantidad:', quantity);

    if (query.length < 3) {
        showAlert('Ingrese al menos 3 caracteres para buscar un producto.', 'warning');
        return;
    }

    if (quantity <= 0) {
        showAlert('Valor inválido. La cantidad debe ser mayor a 0.', 'warning');
        return;
    }

    try {
        console.log('Llamando a buscarProducto con query:', query);
        const products = await buscarProducto(query);
        console.log('Respuesta de buscarProducto:', products);

        if (products && products.length > 0) {
            const product = products[0]; // Tomamos el primer resultado

            console.log('Producto encontrado:', product);

            // Validar stock antes de añadir
            const existingItem = currentSale.saleItems.find(item => item.product.id === product.id);
            const totalQuantity = existingItem ? existingItem.quantity + quantity : quantity;
            
            if (totalQuantity > product.stock) {
                showAlert('Supera el stock disponible', 'danger');
                return;
            }

            if (existingItem) {
                existingItem.quantity += quantity;
                console.log('Producto existente, cantidad actualizada:', existingItem);
            } else {
                currentSale.saleItems.push({ product, quantity });
                console.log('Nuevo producto añadido:', { product, quantity });
            }
            renderCurrentSaleProductsTable();
            productSearchInput.value = ''; // Limpiar campo de búsqueda
            quantityInput.value = 1; // Resetear cantidad
        } else {
            showAlert('Producto no encontrado.', 'danger');
            console.log('No se encontraron productos para la query:', query);
        }
    } catch (error) {
        console.error('Error al añadir producto:', error);
        showAlert('Error al añadir producto', 'danger');
    }
};

const removeProductFromCurrentSale = (productId) => {
    currentSale.saleItems = currentSale.saleItems.filter(item => item.product.id !== productId);
    renderCurrentSaleProductsTable();
};

// Funciones de API
const buscarProducto = async (query) => {
    try {
        const response = await axios.get(`${API_URL}/api/products/search?codigo=${query}`);
        return response.data;
    } catch (error) {
        console.error('Error al buscar producto:', error);
        return null;
    }
};

const crearVenta = async () => {
    try {
        const saleItemsForRequest = currentSale.saleItems.map(item => ({
            productId: item.product.id,
            quantity: item.quantity
        }));

        const ventaData = {
            saleItems: saleItemsForRequest,
            descuento: currentSale.descuento
        };

        const response = await axios.post(`${API_URL}/api/sales`, ventaData);
        const venta = response.data;
        console.log('Venta creada:', venta);

        showAlert('Detalle de Venta ID: ' + venta.id + ' - Total: ' + formatCurrency(venta.totalConIva), 'info');

        // Actualizar la lista de productos para reflejar el nuevo stock
        if (typeof cargarProductos === 'function') {
            await cargarProductos();
        }

        // Reiniciar el formulario de venta actual después de guardar
        currentSale = {
            saleItems: [],
            descuento: 0
        };
        document.getElementById('newSaleForm').reset();
        document.getElementById('current-sale-products-table-body').innerHTML = '';
        document.getElementById('current-sale-total').textContent = formatCurrency(0);
        document.getElementById('descuento-input').value = 0;
    } catch (error) {
        console.error('Error al crear la venta:', error);
        if (error.response && error.response.data) {
            showAlert(error.response.data, 'danger');
        } else {
            showAlert('Error al crear la venta', 'danger');
        }
    }
};

const cargarVentas = async (startDate = null, endDate = null) => {
    try {
        let url = `${API_URL}/api/sales`;
        if (startDate && endDate) {
            url = `${API_URL}/api/sales/filter-by-date?startDate=${startDate}&endDate=${endDate}`;
        }

        const response = await axios.get(url);
        const ventas = response.data;
        const tbody = document.getElementById('sales-table-body');
        tbody.innerHTML = '';
        
        if (ventas.length === 0 && (startDate || endDate)) {
            showAlert('No se encontraron ventas para el rango de fechas seleccionado.', 'info');
        } else if (ventas.length === 0) {
            showAlert('No hay ventas registradas.', 'info');
        }

        ventas.forEach(venta => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${venta.id}</td>
                <td>${formatDate(venta.fecha)}</td>
                <td>${formatCurrency(venta.subtotal)}</td>
                <td>${formatCurrency(venta.iva)}</td>
                <td>${formatCurrency(venta.descuento)}</td>
                <td>${formatCurrency(venta.totalConIva)}</td>
                <td>${venta.saleItems.length} ítems</td>
                <td>
                    <button class="btn btn-sm btn-info" onclick="verDetalleVenta(${venta.id})">
                        <i class="bi bi-eye"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
        return ventas; // Devolver las ventas para la exportación
    } catch (error) {
        console.error('Error al cargar ventas:', error);
        showAlert('Error al cargar las ventas.', 'danger');
        return []; // Devolver un array vacío en caso de error
    }
};

const verDetalleVenta = async (ventaId) => {
    try {
        const response = await axios.get(`${API_URL}/api/sales/${ventaId}/details`);
        const venta = response.data;
        
        // Aquí puedes implementar la lógica para mostrar el detalle de la venta
        // Por ejemplo, en un modal o en una nueva página más detallada
        console.log('Detalle de venta:', venta);
        showAlert('Detalle de Venta ID: ' + venta.id + ' - Total: ' + formatCurrency(venta.totalConIva), 'info');
    } catch (error) {
        console.error('Error al obtener detalle de venta:', error);
        showAlert('Error al obtener detalle de venta', 'danger');
    }
};

const exportSalesToCsv = async () => {
    try {
        const sales = await cargarVentas(); // Obtener los datos de ventas

        if (sales.length === 0) {
            showAlert('No hay ventas para exportar.', 'warning');
            return;
        }

        let csvContent = "ID,Fecha,Subtotal,IVA,Descuento,Total con IVA,Productos Vendidos\n";

        sales.forEach(sale => {
            const productDescriptions = sale.saleItems.map(item => 
                `"${item.product.description} (x${item.quantity})"`
            ).join("; ");

            const row = [
                sale.id,
                formatDate(sale.fecha),
                sale.subtotal,
                sale.iva,
                sale.descuento,
                sale.totalConIva,
                `"${productDescriptions}"`
            ].join(',');
            csvContent += row + "\n";
        });

        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = 'ventas_milimothitas.csv';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        showAlert('Ventas exportadas a CSV exitosamente.', 'success');

    } catch (error) {
        console.error('Error al exportar ventas a CSV:', error);
        showAlert('Error al exportar ventas a CSV.', 'danger');
    }
};

const filterSalesByDate = () => {
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');

    const startDate = startDateInput.value;
    const endDate = endDateInput.value;

    if (!startDate || !endDate) {
        showAlert('Por favor, seleccione una fecha de inicio y una fecha final.', 'warning');
        return;
    }

    // Las fechas HTML input[type="date"] son en formato YYYY-MM-DD
    // Necesitamos convertirlas a un formato compatible con LocalDateTime en el backend (yyyy-MM-ddTHH:mm:ss)
    const startDateTime = `${startDate}T00:00:00`;
    const endDateTime = `${endDate}T23:59:59`; // Para incluir todo el día final

    cargarVentas(startDateTime, endDateTime);
};

// Event Listeners para la funcionalidad de Ventas
document.addEventListener('DOMContentLoaded', () => {
    console.log('DOMContentLoaded en sales.js disparado.');
    // Cargar ventas al iniciar (la función showSection en app.js llamará a cargarVentas cuando se cambie a la sección de reportes)

    // Event listener para el botón de nueva venta
    const newSaleBtn = document.getElementById('new-sale-btn');
    newSaleBtn.addEventListener('click', () => {
        currentSale = {
            saleItems: [],
            descuento: 0
        };
        document.getElementById('newSaleForm').reset();
        document.getElementById('current-sale-products-table-body').innerHTML = '';
        document.getElementById('current-sale-total').textContent = formatCurrency(0);
        document.getElementById('descuento-input').value = 0; // Resetear descuento
    });

    // Event listener para el botón "Añadir Producto" en la sección de ventas
    const addProductToSaleBtn = document.getElementById('add-product-to-sale');
    if (addProductToSaleBtn) {
        console.log('Botón "Añadir Producto" encontrado.');
        addProductToSaleBtn.addEventListener('click', addProductToCurrentSale);
    } else {
        console.error('Botón "Añadir Producto" NO encontrado.', addProductToSaleBtn);
    }

    // Event listener para el input de búsqueda de producto (Enter key)
    const productSearchInput = document.getElementById('sale-product-search-input');
    if (productSearchInput) {
        productSearchInput.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault(); // Evitar que el Enter envíe el formulario
                addProductToCurrentSale();
            }
        });
    }

    // Event listener para el input de descuento
    const descuentoInput = document.getElementById('descuento-input');
    if (descuentoInput) {
        descuentoInput.addEventListener('input', (e) => {
            currentSale.descuento = parseFloat(e.target.value) || 0;
            calculateSaleTotals();
        });
    }

    // Event listener para el botón Exportar CSV
    const exportCsvButton = document.getElementById('export-sales-csv');
    if (exportCsvButton) {
        exportCsvButton.addEventListener('click', exportSalesToCsv);
    }

    // Event listener para el botón Filtrar Ventas por Fecha
    const filterSalesButton = document.getElementById('filter-sales-by-date');
    if (filterSalesButton) {
        filterSalesButton.addEventListener('click', filterSalesByDate);
    }
}); 