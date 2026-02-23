<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Vector Search</title>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- Select2 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <!-- Select2 JS -->
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

    <style>
        body { font-family: Arial; margin: 40px; }
        .select2-container { width: 300px !important; }

        table {
            margin-top: 30px;
            border-collapse: collapse;
            width: 60%;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #eee;
        }

        .remove-btn {
            color: red;
            cursor: pointer;
            font-weight: bold;
        }
    </style>
</head>

<body>

<h2>Vector Search</h2>

<select id="searchSelect" style="width: 300px;">
    <option></option>
</select>

<h3>Selected Items</h3>

<table id="selectedTable">
    <thead>
        <tr>
            <th>Product Details</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody></tbody>
</table>

<script>
$(document).ready(function() {

    // Initialize Select2
    $('#searchSelect').select2({
        placeholder: "Search products...",
        allowClear : true,
        ajax: {
            delay: 300,
            url: "${pageContext.request.contextPath}/product/ajax/search",
            dataType: 'json',
            data: function(params) {
                return { q: params.term }; // search query
            },
            processResults: function(data) {
                // Convert API results to Select2 format
                return {
                    results: data.map(item => ({
                        id: item.displayText,
                        text: item.displayText
                    }))
                };
            }
        }
    });

    // On select, add to table
    $('#searchSelect').on('select2:select', function(e) {
        const text = e.params.data.text;
        addToTable(text);

        // Reset select box
        $('#searchSelect').val(null).trigger('change');
    });
});

// 🔥 Add Row To Table (with duplicate prevention)
function addToTable(text) {

    const tableBody = document
        .getElementById("selectedTable")
        .getElementsByTagName("tbody")[0];

    // Prevent duplicate
    const rows = tableBody.getElementsByTagName("tr");
    for (let i = 0; i < rows.length; i++) {
        if (rows[i].cells[0].innerText === text) {
            alert("Already added!");
            return;
        }
    }

    const newRow = tableBody.insertRow();
    const cell1 = newRow.insertCell(0);
    const cell2 = newRow.insertCell(1);

    cell1.innerText = text;
    cell2.innerHTML = "<span class='remove-btn' onclick='removeRow(this)'>X</span>";
}

// 🔥 Remove Row
function removeRow(btn) {
    const row = btn.parentNode.parentNode;
    row.parentNode.removeChild(row);
}
</script>

</body>
</html>