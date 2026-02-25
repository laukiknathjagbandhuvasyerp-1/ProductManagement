<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Vector Search</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <style>
        body { margin: 20px; font-family: Arial; }
        .select2 { width: 300px; }
        table { margin-top: 20px; border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background: #f2f2f2; }
        .delete { color: red; cursor: pointer; margin-right: 10px; }
        .remove { color: gray; cursor: pointer; }
        .toast { position: fixed; top: 20px; right: 20px; padding: 10px; background: green; color: white; display: none; }
        .toast.error { background: red; }
    </style>
</head>
<body>

<h2>Vector Search</h2>

<select id="searchSelect" class="select2"></select>

<table id="itemsTable">
    <thead>
        <tr>
            <th>Product Details</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody></tbody>
</table>

<div id="toast" class="toast"></div>

<script>
$(document).ready(function() {

    $('#searchSelect').select2({
        placeholder: "Search products...",
        minimumInputLength: 2,
        ajax: {
            url: '${pageContext.request.contextPath}/product/ajax/search',
            dataType: 'json',
            data: function(params) {
                return { q: params.term };
            },
            processResults: function(data) {
                return {
                    results: data.map(item => ({
                        id: item.variantId,
                        text: item.displayText,
                        variantId: item.variantId,
                        productId: item.productId
                    }))
                };
            }
        }
    });

    $('#searchSelect').on('select2:select', function(e) {
        const data = e.params.data;
        addRow(data.text, data.variantId, data.productId);
        $('#searchSelect').val(null).trigger('change');
    });
});

// Add row
function addRow(text, variantId, productId) {
    const tbody = $('#itemsTable tbody');

    // Check duplicate
    let exists = false;
    tbody.find('tr').each(function() {
        if ($(this).data('variant-id') == variantId) exists = true;
    });

    if (exists) {
        showToast('Already added!', 'error');
        return;
    }

    const row = $('<tr>').data('variant-id', variantId).data('product-id', productId);
    row.append('<td>' + text + '</td>');
    // 🔥 SINGLE DELETE BUTTON - Only variant delete
    row.append('<td>' +
        '<span class="delete" onclick="deleteItem(this, ' + variantId + ')">🗑️ Delete</span>' +
        '<span class="remove" onclick="removeItem(this)">❌ Remove</span>' +
        '</td>');

    tbody.append(row);
    showToast('Added to list');
}

// 🔥 Delete variant only
function deleteItem(btn, variantId) {
    if (!confirm('Delete this item?')) return;

    $.ajax({
        url: '${pageContext.request.contextPath}/product/delete/variant/' + variantId,
        type: 'POST',
        success: function() {
            $(btn).closest('tr').remove();
            showToast('Deleted successfully');
        },
        error: function() {
            showToast('Delete failed', 'error');
        }
    });
}

// Remove from list only
function removeItem(btn) {
    if (confirm('Remove from list only?')) {
        $(btn).closest('tr').remove();
        showToast('Removed from list');
    }
}

// Toast notification
function showToast(msg, type) {
    const toast = $('#toast');
    toast.text(msg).removeClass('error');
    if (type === 'error') toast.addClass('error');
    toast.show();
    setTimeout(() => toast.hide(), 2000);
}
</script>

</body>
</html>