import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-slider',
  imports: [CommonModule],
  templateUrl: './slider.html',
  styleUrls: ['./slider.css']
})
export class Slider {

currentIndex = 0;

slides = [
  {
    img: '/img/Principal2.jpg',
    title: 'Machu Picchu',
    date: '29 de diciembre al 9 de enero de 2026',
    desc: 'Ciudadela inca famosa por su ubicación en los Andes peruanos.'
  },
  {
    img: '/img/Principal4.jpg',
    title: 'Playa Caribeña',
    date: '12 al 20 de febrero de 2026',
    desc: 'Aguas cristalinas y arena blanca para unas vacaciones perfectas.'
  },
  {
    img: '/img/Principal3.jpg',
    title: 'Estambul',
    date: '5 al 15 de marzo de 2026',
    desc: 'Una mezcla de culturas, historia y arquitectura impresionante.'
  },
];

nextSlide() {
  this.currentIndex = (this.currentIndex + 1) % this.slides.length;
}

prevSlide() {
  this.currentIndex = (this.currentIndex - 1 + this.slides.length) % this.slides.length;
} 
}
