import { Component } from '@angular/core';
import { Header } from '../../header/header';
import { Footer } from '../../footer/footer';
import { Slider } from '../../components/slider/slider';

@Component({
  selector: 'app-home',
  imports: [Header, Footer, Slider],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

}
