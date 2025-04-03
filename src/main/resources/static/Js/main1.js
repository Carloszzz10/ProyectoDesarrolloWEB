const cloud = document.getElementById('cloud');
const barralateral = document.querySelector(".barralateral");
const spans = document.querySelectorAll('span');
const modooscuro = document.querySelector('.switch');
const circulo = document.querySelector(".circulo");
const menu = document.querySelector(".menu");
const main = document.querySelector('main');
const botonregistro = document.querySelector(".botonusuario");
window.addEventListener('DOMContentLoaded', () => {
    barralateral.classList.remove('minibarralateral');
    spans.forEach(span => {
        span.classList.remove('oculto');
    });
    aplicarModoOscuro();  //nuevo
});


cloud.addEventListener('click', () => {
    // alert('Hola funciona?')
    barralateral.classList.toggle('minibarralateral');
    main.classList.toggle('minimain');
    if (barralateral.classList.contains('minibarralateral')) {
        botonregistro.style.display = "none";
        spans.forEach((span) => {
            span.classList.toggle('oculto');
        });
    } else {
        botonregistro.style.display = "flex";
        spans.forEach((span) => {
            span.classList.remove('oculto');
        });
    }
});


modooscuro.addEventListener("click", () => {
    let body = document.body;
    body.classList.toggle('dark-mode');
    circulo.classList.toggle("prendido");
    localStorage.setItem('modoOscuro', document.body.classList.contains('dark-mode') ? 'activo' : 'inactivo'); //nuevo
});

function aplicarModoOscuro() {  //nuevo
    if (localStorage.getItem('modoOscuro') === 'activo') {
        document.body.classList.add('dark-mode');
        circulo.classList.add('prendido');
    }
}

menu.addEventListener('click', () => {
    if (window.innerWidth <= 600) {
        botonregistro.style.display = "none";
    }
    barralateral.classList.toggle("maxbarralateral");
    if (barralateral.classList.contains('maxbarralateral')) {
        main.classList.add('minimain');
        barralateral.classList.add('minibarralateral');
        spans.forEach((span) => {
            span.classList.add('oculto');
        }
        );
    }
});
