import { Component } from '@angular/core';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { CommonModule } from '@angular/common';
import { Header } from "../../header/header";
import { Footer } from "../../footer/footer";

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [FullCalendarModule, CommonModule, Header, Footer],
  templateUrl: './reservas.html',
  styleUrls: ['./reservas.css'],
})
export class Reservas {

  reservas = [
    {
      id: 1,
      title: 'Reserva 1',
      paquetes: ['Paquete 5', 'Paquete 3'],
      start: '2026-01-02',
      end: '2026-01-16',
      color: 'red'
    },
    {
      id: 2,
      title: 'Reserva 2',
      paquetes: ['Paquete 1', 'Paquete 2'],
      start: '2026-01-27',
      end: '2026-01-31',
      color: 'yellow'
    }
  ];

  events = this.reservas.map(r => ({
    title: r.title,
    start: r.start,
    end: r.end,
    color: r.color
  }));

 calendarOptions = {
  plugins: [dayGridPlugin, interactionPlugin],
  initialView: 'dayGridMonth',
  events: this.events,
  height: 'auto'
};

}