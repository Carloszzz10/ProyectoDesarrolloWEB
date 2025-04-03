function validarFormulario(event) {
    event.preventDefault();

    document.getElementById("errorMotivo").textContent = "";
    document.getElementById("errorDetalle").textContent = "";

    const motivo = document.getElementsByName("motivo")[0].value;
    const detalle = document.getElementsByName("detalle")[0].value.trim();
    let esValido = true;

    if (motivo === "Seleccionar") {
        document.getElementById("errorMotivo").textContent = "Por favor, seleccione un motivo válido.";
        esValido = false;
    }

    if (detalle === "") {
        document.getElementById("errorDetalle").textContent = "El campo 'Detalle' no puede estar vacío.";
        esValido = false;
    }

    if (esValido) {
        document.getElementById("contactoForm").submit();
    }
}

function confirmDelete() {
    return confirm("¿Estás seguro de que deseas eliminar este reporte de contacto?");
}

function filtrarReportes() {
    var motivoSeleccionado = document.getElementById("filtroMotivo").value;
    var contactos = document.querySelectorAll(".list-group-item");

    contactos.forEach(function(contacto) {
        var motivo = contacto.querySelector(".motivo").textContent;

        if (motivoSeleccionado === "todos" || motivo === motivoSeleccionado) {
            contacto.style.display = "block";
        } else {
            contacto.style.display = "none";
        }
    });
}