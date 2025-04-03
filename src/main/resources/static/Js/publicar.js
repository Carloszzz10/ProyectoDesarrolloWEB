//Seccion para hacer resizable la modal

const textarea = document.getElementById("autoResizeTextarea");

function ajustarAltura() {
    textarea.style.height = 'auto'; // Restablece la altura para calcular el scrollHeight
    textarea.style.height = `${textarea.scrollHeight}px`; // Ajusta la altura al scrollHeight
}

textarea.addEventListener('input', ajustarAltura);
ajustarAltura(); // Ajusta la altura al cargar la página



//Seccion para abrir la modal
const mostrarButton = document.getElementById("updateDetails");
const cancelarButton = document.getElementById("cancel");
const myModal = document.getElementById("favDialog");

mostrarButton.addEventListener("click", function () {

      window.location.href = '/?mostrarModal=true';
    textarea.focus();
});

document.addEventListener("DOMContentLoaded", function() {
    const myModal = document.getElementById("favDialog");

    // Comprobar si el parámetro 'mostrarModal' está presente en la URL
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('mostrarModal')) {
        myModal.showModal(); // Mostrar el modal
    }
});


cancelarButton.addEventListener("click", function () {
    myModal.close();
});

//Seccion de la modal para editar

function abrirModalEditar(postId, contenido, imageUrl) {
    const modal = document.getElementById('editDialog');
    const contenidoTextarea = modal.querySelector('textarea[name="contenido"]');
    const postIdInput = modal.querySelector('input[name="postId"]');
    const fileInfo = document.getElementById('fileInfoEditar');

    // Asignar valores al modal
    contenidoTextarea.value = contenido;
    postIdInput.value = postId;
    fileInfo.textContent = imageUrl ? imageUrl.split('/').pop() : 'No hay imagen';
    
    modal.showModal();
}


document.getElementById("cancelEdit").addEventListener("click", function() {
    const modal = document.getElementById('editDialog');
    modal.close();
});


function abrirInputFile() {
    var file = document.getElementById("file").click();
}

function showFileName() {
    const input = document.getElementById('file');
    const fileInfo = document.getElementById('fileInfo');

    // Verificamos si hay un archivo seleccionado
    if (input.files.length > 0) {
        fileInfo.textContent = input.files[0].name;  // Mostramos el nombre del archivo
    } else {
        fileInfo.textContent = "";  // Si no hay archivos
    }
}


function abrirInputFileEditar() {
    var file = document.getElementById("fileEditar").click();
}

function showFileNameEditar() {
    const input = document.getElementById('fileEditar');
    const fileInfo = document.getElementById('fileInfoEditar');

    // Verificamos si hay un archivo seleccionado
    if (input.files.length > 0) {
        fileInfo.textContent = input.files[0].name;  // Mostramos el nombre del archivo
    } else {
        fileInfo.textContent = "";  // Si no hay archivos
    }
}



        const areaTexto = document.getElementById('autoResizeTextarea');
        const contadorCaracteres = document.getElementById('charCount');
        const maximoCaracteres = 400;
        const archivoInput = document.getElementById('file');
        const informacionArchivo = document.getElementById('fileInfo');
        const formulario = document.querySelector('form');

     
        areaTexto.addEventListener('input', () => {
            const caracteresRestantes = maximoCaracteres - areaTexto.value.length;
            contadorCaracteres.textContent = `${caracteresRestantes} caracteres restantes`;

            if (caracteresRestantes < 0) {
                contadorCaracteres.style.color = 'red'; // Advertencia si se excede el límite
            } else {
                contadorCaracteres.style.color = '#999'; // Color normal
            }
        });

   

   
        formulario.addEventListener('submit', (evento) => {
            let valido = true;

         
            if (areaTexto.value.trim() === '') {
                alert('El campo de texto no puede estar vacío.');
                valido = false;
            } else if (areaTexto.value.length > maximoCaracteres) {
                alert('El texto supera el límite de caracteres permitido.');
                valido = false;
            }

       
            const archivo = archivoInput.files[0];
            if (archivo) {
                const tiposPermitidos = ['image/jpeg', 'image/png', 'image/gif'];
                if (!tiposPermitidos.includes(archivo.type)) {
                    alert('Por favor, selecciona un archivo de imagen válido (jpg, png, gif).');
                    valido = false;
                }
            }

       
            if (!valido) {
                evento.preventDefault();
            }
        });


