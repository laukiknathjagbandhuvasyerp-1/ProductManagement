<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product Search</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 20px;
        }

        h2 {
            margin-bottom: 20px;
            color: #333;
        }

        /* Search box with dropdown */
        .search-container {
            position: relative;
            margin-bottom: 20px;
        }

        .search-box {
            display: flex;
            gap: 10px;
        }

        .search-box input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        .search-box button {
            padding: 10px 20px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .search-box button:hover {
            background: #0056b3;
        }

        /* Dropdown styles */
        .dropdown {
            position: absolute;
            top: 100%;
            left: 0;
            right: 0;
            background: white;
            border: 1px solid #ddd;
            border-top: none;
            border-radius: 0 0 4px 4px;
            max-height: 300px;
            overflow-y: auto;
            display: none;
            z-index: 1000;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .dropdown-item {
            padding: 10px 15px;
            cursor: pointer;
            border-bottom: 1px solid #eee;
            transition: background 0.2s;
        }

        .dropdown-item:hover {
            background: #f0f8ff;
        }

        .dropdown-item strong {
            color: #007bff;
        }

        .dropdown-item .item-info {
            font-size: 12px;
            color: #666;
            margin-top: 4px;
        }

        .dropdown-item .item-info span {
            margin-right: 10px;
        }

        .loading-dropdown {
            padding: 10px;
            text-align: center;
            color: #666;
        }

        /* Table */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th {
            background: #007bff;
            color: white;
            padding: 12px;
            text-align: left;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background: #f5f5f5;
        }

        /* Action buttons */
        .add-btn {
            padding: 5px 10px;
            background: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .add-btn:hover {
            background: #218838;
        }

        .remove-btn {
            padding: 5px 10px;
            background: #dc3545;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .remove-btn:hover {
            background: #c82333;
        }

        /* Pagination */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
        }

        .pagination button {
            padding: 8px 16px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .pagination button:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .pagination span {
            font-weight: bold;
        }

        /* Loading */
        .loading {
            text-align: center;
            padding: 20px;
            display: none;
        }

        .no-data {
            text-align: center;
            padding: 20px;
            color: #666;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>🔍 Product Search</h2>

    <!-- Search Box with Dropdown - FIXED -->
    <div class="search-container">
        <div class="search-box">
            <input type="text" id="searchInput" placeholder="Search products..." autofocus>
            <button onclick="searchProducts(1)">Search</button>
        </div>

        <!-- Dropdown for suggestions -->
        <div id="dropdown" class="dropdown"></div>
    </div>

    <!-- Loading -->
    <div id="loading" class="loading">Loading...</div>

    <!-- Product Table -->
    <table id="productTable">
        <thead>
            <tr>
                <th>ID</th>
                <th>Product Name</th>
                <th>Description</th>
                <th>Brand</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody id="tableBody">
            <tr>
                <td colspan="5" class="no-data">Enter search term to find products</td>
            </tr>
        </tbody>
    </table>

    <!-- Pagination -->
    <div class="pagination">
        <button id="prevBtn" onclick="changePage(-1)" disabled>Previous</button>
        <span id="pageInfo">Page 1 of 1</span>
        <button id="nextBtn" onclick="changePage(1)" disabled>Next</button>
    </div>
</div>

<script>
    let currentPage = 1;
    let totalPages = 1;
    let searchTimeout;

    const searchInput = document.getElementById('searchInput');
    const dropdown = document.getElementById('dropdown');

    // Input event for autocomplete
    searchInput.addEventListener('input', function(e) {
        const searchTerm = e.target.value.trim();

        clearTimeout(searchTimeout);

        if(searchTerm.length < 2) {
            dropdown.style.display = 'none';
            return;
        }

        searchTimeout = setTimeout(() => {
            fetchSuggestions(searchTerm);
        }, 300);
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', function(e) {
        if(!searchInput.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    });

    // Enter key event
    searchInput.addEventListener('keypress', function(e) {
        if(e.key === 'Enter') {
            searchProducts(1);
        }
    });

    // Fetch suggestions from server
    function fetchSuggestions(searchTerm) {
        const url = `/product/ajax/products/suggest?p=${encodeURIComponent(searchTerm)}`;

        dropdown.innerHTML = '<div class="loading-dropdown">Loading suggestions...</div>';
        dropdown.style.display = 'block';

        fetch(url)
            .then(res => res.json())
            .then(products => {
                if(products.length === 0) {
                    dropdown.innerHTML = '<div class="loading-dropdown">No products found</div>';
                    return;
                }
                renderSuggestions(products, searchTerm);
            })
            .catch(err => {
                console.error('Error fetching suggestions:', err);
                dropdown.innerHTML = '<div class="loading-dropdown">Error loading suggestions</div>';
            });
    }

    // Render dropdown suggestions
    function renderSuggestions(products, searchTerm) {
        let html = '';
        const regex = new RegExp(`(${searchTerm})`, 'gi');

        products.forEach(p => {
            const highlightedName = p.productName.replace(regex, '<strong>$1</strong>');

            // Escape single quotes for onclick
            const safeName = p.productName.replace(/'/g, "\\'");
            const safeDesc = (p.productDescription || '').replace(/'/g, "\\'");
            const safeBrand = (p.productBrandName || '').replace(/'/g, "\\'");

            html += `
                <div class="dropdown-item" onclick="addToTable(${p.productId}, '${safeName}', '${safeDesc}', '${safeBrand}')">
                    <div>${highlightedName}</div>
                    <div class="item-info">
                        <span>🏷️ ${p.productBrandName || 'No Brand'}</span>
                        <span>📦 ID: ${p.productId}</span>
                    </div>
                </div>
            `;
        });

        dropdown.innerHTML = html;
    }

    // Add product to table
    function addToTable(id, name, description, brand) {
        // Check if already in table
        const existingRows = document.querySelectorAll('#tableBody tr');
        for(let row of existingRows) {
            if(row.cells && row.cells[0] && row.cells[0].innerText == id) {
                alert('Product already in table!');
                dropdown.style.display = 'none';
                searchInput.value = '';
                return;
            }
        }

        // Add new row
        const tbody = document.getElementById('tableBody');

        // Remove "no data" message if present
        if(tbody.children.length === 1 && tbody.children[0].classList.contains('no-data')) {
            tbody.innerHTML = '';
        }

        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${id}</td>
            <td>${name}</td>
            <td>${description || '-'}</td>
            <td>${brand || '-'}</td>
            <td><button class="remove-btn" onclick="removeFromTable(this)">Remove</button></td>
        `;

        tbody.appendChild(newRow);

        // Hide dropdown and clear input
        dropdown.style.display = 'none';
        searchInput.value = '';

        // Update pagination visibility
        updatePaginationVisibility();
    }

    // Remove product from table
    function removeFromTable(btn) {
        const row = btn.closest('tr');
        row.remove();

        // Show "no data" message if table is empty
        const tbody = document.getElementById('tableBody');
        if(tbody.children.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="no-data">No products added yet</td></tr>';
        }

        updatePaginationVisibility();
    }

    // Show/hide pagination based on table content
    function updatePaginationVisibility() {
        const tbody = document.getElementById('tableBody');
        const pagination = document.querySelector('.pagination');

        if(tbody.children.length === 1 && tbody.children[0].classList.contains('no-data')) {
            pagination.style.display = 'none';
        } else {
            pagination.style.display = 'flex';
        }
    }

    // Search products
    function searchProducts(page) {
        let searchTerm = document.getElementById('searchInput').value.trim();

        document.getElementById('loading').style.display = 'block';
        document.getElementById('tableBody').innerHTML = '';

        let url = `/product/ajax/products/search?page=${page}`;
        if(searchTerm){
            url += `&p=${encodeURIComponent(searchTerm)}` ;
        }

        fetch(url)
            .then(res => {
                if(!res.ok) throw new Error('Error');
                return res.json();
            })
            .then(data => {
                document.getElementById('loading').style.display = 'none';

                currentPage = data.number + 1;
                totalPages = data.totalPages;

                renderTable(data.content);
                updatePagination();

                console.log(`Found ${data.totalElements} products`);
            })
            .catch(err => {
                console.error(err);
                document.getElementById('loading').style.display = 'none';
                document.getElementById('tableBody').innerHTML =
                    '<tr><td colspan="5" class="no-data">Error loading products</td></tr>';
            });
    }

    // Render table
    function renderTable(products) {
        let tbody = document.getElementById('tableBody');

        if(!products || products.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="no-data">No products found</td></tr>';
            return;
        }

        let html = '';
        products.forEach(p => {
            html += `<tr>
                <td>${p.productId}</td>
                <td>${p.productName || '-'}</td>
                <td>${p.productDescription || '-'}</td>
                <td>${p.productBrandName || '-'}</td>
                <td><button class="add-btn" onclick="addToTable(${p.productId}, '${p.productName.replace(/'/g, "\\'")}', '${(p.productDescription || '').replace(/'/g, "\\'")}', '${(p.productBrandName || '').replace(/'/g, "\\'")}')">Add</button></td>
            </tr>`;
        });

        tbody.innerHTML = html;
    }

    // Change page
    function changePage(delta) {
        let newPage = currentPage + delta;
        if(newPage >= 1 && newPage <= totalPages) {
            searchProducts(newPage);
        }
    }

    // Update pagination
    function updatePagination() {
        document.getElementById('pageInfo').innerText = `Page ${currentPage} of ${totalPages}`;
        document.getElementById('prevBtn').disabled = (currentPage <= 1);
        document.getElementById('nextBtn').disabled = (currentPage >= totalPages);
    }
</script>

</body>
</html>