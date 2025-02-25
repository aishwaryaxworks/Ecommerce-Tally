let selectedCustomerEmail = '';
let selectedCustomerName= '';
    async function showSuggestions(value) {
        const suggestionsDiv = document.getElementById('suggestions');
        suggestionsDiv.innerHTML = '';

        if (value.length < 3) {
            suggestionsDiv.style.display = 'none';
            return;
        }
        try {
            const response = await fetch(`${urlConstant.url}/api/customer/suggestions?customerName=${encodeURIComponent(value)}`);
            const suggestions = await response.json();

            if (suggestions.length > 0) {
                suggestionsDiv.innerHTML = suggestions.map(suggestion => `
                    <div class="suggestion-item" onclick="selectSuggestion('${suggestion.customerName}', '${suggestion.customerEmailId}')">
                        <strong>${suggestion.customerName}</strong> <br>
                        <small>${suggestion.customerEmailId}</small>
                    </div>
                `).join('');
                suggestionsDiv.style.display = 'block';
            } else {
                suggestionsDiv.style.display = 'none';
            }
        } catch (error) {
            console.error('Error fetching suggestions:', error);
        }
    }

    function selectSuggestion(name, email) {
        document.getElementById('customerInput').value = name;
        document.getElementById('suggestions').style.display = 'none';
        selectedCustomerEmail = email;
        selectedCustomerName=name;

    }
    async function searchCustomers() {
        const searchValue = document.getElementById('customerInput').value.trim();

        if (searchValue) {
            try {
                const response = await fetch(`${urlConstant.url}/api/customer/suggestions?customerName=${encodeURIComponent(searchValue)}`);
                if (response.ok) {
                    const customerData = await response.json();
                    displayCustomerData(customerData);
                    updatePagination(1, 0);
                } else {
                    const errorData = await response.json();
                    Swal.fire('Not Found', errorData.errorMessage || 'Customer not found.', 'warning');
                }
            } catch (error) {
                Swal.fire('Error', 'Could not retrieve customer data.', 'error');
            }
        } else {
            Swal.fire('Error', 'Please enter a customer name.', 'error');
        }
    }

    function displayCustomerData(customers) {
        const tableBody = document.getElementById('customerTableBody');
        tableBody.innerHTML = '';

        if (customers && customers.length > 0) {
            customers.forEach(customer => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${customer.customerType || 'N/A'}</td>
                    <td>${customer.customerName || 'N/A'}</td>
                    <td>${customer.customerEmailId || 'N/A'}</td>
                    <td>${customer.contactNumber || 'N/A'}</td>
                    <td>${customer.gstNumber || 'N/A'}</td>
                    <td>${customer.city || 'N/A'}</td>
                    <td>${customer.pinCode || 'N/A'}</td>
                    <td>${customer.address1 || 'N/A'}</td>
                    <td>${customer.billingAddress || 'N/A'}</td>
                    <td>
                        <a href="/netzwerk/customer/edit?id=${customer.customerId}" class="btn btn-warning btn-sm">Edit</a>
                        <a href="/netzwerk/customer/delete?id=${customer.customerId}" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        } else {
            const noDataRow = document.createElement('tr');
            noDataRow.innerHTML = `<td colspan="10" class="text-center">No customer data found</td>`;
            tableBody.appendChild(noDataRow);
        }
    }

    function clearSearch() {
        document.getElementById('customerInput').value = '';
        document.getElementById('customerTableBody').innerHTML = '';
        window.location.href = "/netzwerk/customer/toView";
    }

    function updatePagination(totalPages, currentPage) {
        const paginationElement = document.querySelector('.pagination');
        paginationElement.innerHTML = '';

        paginationElement.innerHTML += `<li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="goToPage(${currentPage - 1})">Previous</a>
        </li>`;
        for (let i = 0; i < totalPages; i++) {
            paginationElement.innerHTML += `<li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" onclick="goToPage(${i})">${i + 1}</a>
            </li>`;
        }
        paginationElement.innerHTML += `<li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="goToPage(${currentPage + 1})">Next</a>
        </li>`;
    }


    async function showInventorySuggestions(value) {
        const suggestionsDiv = document.getElementById('inventorySuggestions');
        suggestionsDiv.innerHTML = '';

        if (value.length < 3) {
            suggestionsDiv.style.display = 'none';
            return;
        }
        try {
            const response = await fetch(`${urlConstant.url}/api/inventory/suggestions?make=${encodeURIComponent(value)}`);
            const suggestions = await response.json();
            if (suggestions.length > 0) {
                suggestionsDiv.innerHTML = suggestions.map(suggestion => `
                    <div class="suggestion-item" onclick="selectInventorySuggestion('${suggestion}')">
                        <strong>${suggestion}</strong>
                    </div>
                `).join('');
                suggestionsDiv.style.display = 'block';
            } else {
                console.log('No suggestions found.');
                suggestionsDiv.style.display = 'none';
            }
        } catch (error) {
            console.error('Error while fetching suggestions:', error);
            suggestionsDiv.style.display = 'none';
        }
    }

function selectInventorySuggestion(make) {
    document.getElementById('makeSearchInput').value = make;
    document.getElementById('inventorySuggestions').style.display = 'none';
}

const pageSize = 25;
let currentPage = 0;

async function searchInventoryByMake(pageNo = 0) {
    const searchValue = document.getElementById('makeSearchInput').value.trim();
    if (searchValue.length < 3) {
        console.log('Search term too short.');
        return;
    }

    try {
        const response = await fetch(`${urlConstant.url}/api/inventory/search-by-make?make=${encodeURIComponent(searchValue)}&pageNo=${pageNo}&pageSize=${pageSize}`);

        if (response.ok) {
            const responseData = await response.json();
            const { content, currentPage, totalPages } = responseData;

            console.log('Search Response:', responseData);
            displayInventoryData(content);
            updatePagination(currentPage, totalPages);
        } else {
            console.error('Error fetching search results:', response.status);
            Swal.fire('Error', 'Could not fetch search results.', 'error');
        }
    } catch (error) {
        console.error('Search error:', error);
        Swal.fire('Error', 'An unexpected error occurred.', 'error');
    }
}

function displayInventoryData(data) {
    const tableBody = document.querySelector('tbody');
    tableBody.innerHTML = '';

    if (data.length > 0) {
        data.forEach(item => {
            const row = `
                <tr>
                    <td>${item.groupName || 'N/A'}</td>
                    <td>${item.subGroupName || 'N/A'}</td>
                    <td>${item.make || 'N/A'}</td>
                    <td>${item.model || 'N/A'}</td>
                    <td>${item.itemName || 'N/A'}</td>
                    <td>${item.productCode || 'N/A'}</td>
                    <td>${item.hsnCode || 'N/A'}</td>
                    <td>${item.openingBalance || 'N/A'}</td>
                    <td>
                        <a href="/netzwerk/inventory/edit/${item.id}" class="btn btn-warning btn-sm">Edit</a>
                        <a href="/netzwerk/inventory/delete/${item.id}" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                </tr>
            `;
            tableBody.innerHTML += row;
        });
    } else {
        const noDataRow = `<tr><td colspan="9" class="text-center">No Inventory Data Found</td></tr>`;
        tableBody.innerHTML = noDataRow;
    }
}

function updatePagination(currentPage, totalPages) {
    const paginationElement = document.querySelector('.pagination');
    paginationElement.innerHTML = '';

    paginationElement.innerHTML += `
        <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="searchInventoryByMake(${currentPage - 1}); return false;">Previous</a>
        </li>
    `;

    for (let i = 0; i < totalPages; i++) {
        paginationElement.innerHTML += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" onclick="searchInventoryByMake(${i}); return false;">${i + 1}</a>
            </li>
        `;
    }

    paginationElement.innerHTML += `
        <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="searchInventoryByMake(${currentPage + 1}); return false;">Next</a>
        </li>
    `;
}

function clearInventorySearch() {
    document.getElementById('makeSearchInput').value = '';
    document.getElementById('inventorySuggestions').innerHTML = '';
    window.location.href = "/netzwerk/inventory/viewInventory";
}