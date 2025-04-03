document.addEventListener('DOMContentLoaded', function() {
    const formRegistro = document.getElementById("formRegistro");
    
    formRegistro.addEventListener("submit", function(event) {
    
        event.preventDefault();
        
        limpiarErrores();
        let formValid = true;

       
        const nombre = document.getElementById("nombres").value.trim();
        const apellido = document.getElementById("apellidos").value.trim();
        const telefono = document.getElementById("numero").value.trim();
        const email = document.getElementById("usuario").value.trim(); 
        const password = document.getElementById("password").value.trim();
        const repassword = document.getElementById("repassword").value.trim();
        const ubicacion = document.getElementById("ubicacion").value;

  
        const regexNombre = /^[A-Za-záéíóúÁÉÍÓÚñÑ\s]+$/; 
        const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (nombre === "" || !regexNombre.test(nombre) || nombre.length > 20) {
            mostrarError("errorNombre", "Solo letras(20 caracteres)");
            formValid = false;
        }

        if (apellido === "" || !regexNombre.test(apellido) || apellido.length > 20) {
            mostrarError("errorApellido", "Solo letras(20 caracteres)");
            formValid = false;
        }

        if (telefono === "" || telefono.length !== 9 || !/^\d{9}$/.test(telefono)) {
            mostrarError("errorTelefono", "Solo numero(9 dígitos)");
            formValid = false;
        }

        if (email === "" || !regexEmail.test(email)) {
            mostrarError("errorEmail", "Ingrese un correo electrónico válido.");
            formValid = false;
        }

        if (password === "" || password.length < 6 || password.length > 30) {
            mostrarError("errorPassword", "Tener entre 6 y 30 caracteres.");
            formValid = false;
        }

        if (repassword === "" || password !== repassword) {
            mostrarError("errorRePassword", "Las contraseñas no coinciden.");
            formValid = false;
        }

        if (ubicacion === "Seleccionar") {
            mostrarError("errorUbicacion", "Debe seleccionar una ubicación.");
            formValid = false;
        }

     
        if (formValid) {
            formRegistro.submit();
        }
    });

    function mostrarError(id, mensaje) {
        const errorElement = document.getElementById(id);
        if (errorElement) {
            errorElement.textContent = mensaje;
            errorElement.style.display = 'block';
        }
    }

    function limpiarErrores() {
        const errorTexts = document.querySelectorAll(".mensajeerror");
        errorTexts.forEach(function(element) {
            element.textContent = "";
            element.style.display = 'none';
        });
    }
});


function showFileName() {
    const input = document.getElementById('file');
    const fileInfo = document.getElementById('fileInfo');
    
    if (input.files.length > 0) {
        const file = input.files[0];
        const fileSize = file.size / 1024 / 1024; 
        
        if (fileSize > 2) { 
            fileInfo.textContent = "El archivo es demasiado grande. Máximo 2MB permitido.";
            fileInfo.style.color = "red";
            input.value = ''; 
        } else if (!file.type.startsWith('image/')) {
            fileInfo.textContent = "Por favor, seleccione un archivo de imagen válido.";
            fileInfo.style.color = "red";
            input.value = '';
        } else {
            fileInfo.textContent = file.name;
            fileInfo.style.color = "black";
        }
    } else {
        fileInfo.textContent = "Sin archivos seleccionados";
        fileInfo.style.color = "black";
    }
}
