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

        /* Tabs */
        .tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            border-bottom: 2px solid #ddd;
            padding-bottom: 10px;
        }

        .tab-btn {
            padding: 10px 20px;
            background: #f0f0f0;
            border: none;
            border-radius: 5px 5px 0 0;
            cursor: pointer;
            font-size: 16px;
        }

        .tab-btn.active {
            background: #007bff;
            color: white;
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

        .badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }

        .badge.product {
            background: #28a745;
            color: white;
        }

        .badge.variant {
            background: #ffc107;
            color: black;
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
            margin-top: 20px;
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

    <!-- Tabs -->
    <div class="tabs">
        <button class="tab-btn active" onclick="switchTab('normal')">Normal Search</button>
        <button class="tab-btn" onclick="switchTab('vector')">Vector Search (AI)</button>
    </div>

    <!-- Search Box -->
    <div class="search-container">
        <div class="search-box">
            <input type="text" id="searchInput" placeholder="Search products..." autofocus>
            <button onclick="handleSearch()">Search</button>
        </div>
        <div id="dropdown" class="dropdown"></div>
    </div>

    <!-- Loading -->
    <div id="loading" class="loading">Loading...</div>

    <!-- Results Table -->
    <table id="productTable">
        <thead id="tableHeader">
            <tr>
                <th>Type</th>
                <th>ID</th>
                <th>Name</th>
                <th>Brand</th>
                <th>Details</th>
            </tr>
        </thead>
        <tbody id="tableBody">
            <tr>
                <td colspan="5" class="no-data">Enter search term to find products</td>
            </tr>
        </tbody>
    </table>

    <!-- Pagination (for normal search) -->
    <div id="pagination" class="pagination" style="display: none;">
        <button id="prevBtn" onclick="changePage(-1)" disabled>Previous</button>
        <span id="pageInfo">Page 1 of 1</span>
        <button id="nextBtn" onclick="changePage(1)" disabled>Next</button>
    </div>
</div>

<script>
    let currentPage = 1;
    let totalPages = 1;
    let searchTimeout;
    let currentTab = 'normal';

    const searchInput = document.getElementById('searchInput');
    const dropdown = document.getElementById('dropdown');

    // Tab switching
    function switchTab(tab) {
        currentTab = tab;
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        event.target.classList.add('active');

        // Clear and focus
        searchInput.value = '';
        document.getElementById('tableBody').innerHTML = '<tr><td colspan="5" class="no-data">Enter search term to find products</td></tr>';
        document.getElementById('pagination').style.display = tab === 'normal' ? 'flex' : 'none';
        searchInput.focus();

        // Hide dropdown
        dropdown.style.display = 'none';
    }

    // Input event for autocomplete (only for normal search)
    searchInput.addEventListener('input', function(e) {
        if (currentTab !== 'normal') return;

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
            handleSearch();
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

            html += `
                <div class="dropdown-item" onclick="selectSuggestion(${p.productId}, '${p.productName.replace(/'/g, "\\'")}')">
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

    function selectSuggestion(id, name) {
        searchInput.value = name;
        dropdown.style.display = 'none';
        handleSearch();
    }

    // Main search handler
    function handleSearch() {
        if (currentTab === 'normal') {
            normalSearch(1);
        } else {
            vectorSearch();
        }
    }

    // Normal search (with pagination)
    function normalSearch(page) {
        let searchTerm = searchInput.value.trim();

        document.getElementById('loading').style.display = 'block';

        let url = `/product/ajax/products/search?page=${page}`;
        if(searchTerm) {
            url += `&p=${encodeURIComponent(searchTerm)}`;
        }

        fetch(url)
            .then(res => res.json())
            .then(data => {
                document.getElementById('loading').style.display = 'none';

                currentPage = data.number + 1;
                totalPages = data.totalPages;

                renderNormalResults(data.content);
                updatePagination();
            })
            .catch(err => {
                console.error(err);
                document.getElementById('loading').style.display = 'none';
                document.getElementById('tableBody').innerHTML = '<tr><td colspan="5" class="no-data">Error loading products</td></tr>';
            });
    }

    // Vector search (AI) - FIXED for Map return type
    function vectorSearch() {
        let searchTerm = searchInput.value.trim();
        if (!searchTerm) return;

        document.getElementById('loading').style.display = 'block';

        fetch(`/product/ajax/vector-search?q=${encodeURIComponent(searchTerm)}`)
            .then(res => res.json())
            .then(results => {
                console.log("🔥 Results from server:", results);
                document.getElementById('loading').style.display = 'none';
                renderVectorResults(results);
            })
            .catch(err => {
                console.error(err);
                document.getElementById('loading').style.display = 'none';
                document.getElementById('tableBody').innerHTML = '<tr><td colspan="5" class="no-data">Error loading results</td></tr>';
            });
    }

    // Render normal search results
    function renderNormalResults(products) {
        let tbody = document.getElementById('tableBody');

        if(!products || products.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="no-data">No products found</td></tr>';
            return;
        }

        let html = '';
        products.forEach(p => {
            html += `<tr>
                <td><span class="badge product">PRODUCT</span></td>
                <td>${p.productId || ''}</td>
                <td>${p.productName || '-'}</td>
                <td>${p.productBrandName || '-'}</td>
                <td>${p.productDescription || '-'}</td>
            </tr>`;
        });
        tbody.innerHTML = html;
    }

    // 🔥 FIXED: Render vector search results for Map return type
    function renderVectorResults(results) {
        let tbody = document.getElementById('tableBody');

        if(!results || results.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="no-data">No results found</td></tr>';
            return;
        }

        let html = '';
        results.forEach(item => {
            // Skip if item is null/undefined
            if (!item) return;

            // Check if it's from product table (has product_id but no product_variant_id)
            if (item.product_id && !item.product_variant_id) {
                html += `<tr>
                    <td><span class="badge product">PRODUCT</span></td>
                    <td>${item.product_id || ''}</td>
                    <td>${item.product_name || '-'}</td>
                    <td>${item.product_brand_name || '-'}</td>
                    <td>${item.product_description || '-'}</td>
                </tr>`;
            }
            // Check if it's from variant table (has product_variant_id)
            else if (item.product_variant_id) {
                html += `<tr>
                    <td><span class="badge variant">VARIANT</span></td>
                    <td>${item.product_variant_id || ''}</td>
                    <td>${item.product_variant_name || '-'} (${item.product_name || ''})</td>
                    <td>${item.product_brand_name || ''}</td>
                    <td>Product ID: ${item.product_id || ''}</td>
                </tr>`;
            }
        });

        tbody.innerHTML = html;
    }

    // Change page
    function changePage(delta) {
        let newPage = currentPage + delta;
        if(newPage >= 1 && newPage <= totalPages) {
            normalSearch(newPage);
        }
    }

    // Update pagination
    function updatePagination() {
        document.getElementById('pageInfo').innerText = `Page ${currentPage} of ${totalPages}`;
        document.getElementById('prevBtn').disabled = (currentPage <= 1);
        document.getElementById('nextBtn').disabled = (currentPage >= totalPages);
        document.getElementById('pagination').style.display = 'flex';
    }
</script>

</body>
</html>