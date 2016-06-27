import {Component, Input, Output, EventEmitter, ElementRef, ViewChild, OnChanges} from '@angular/core';
import {CORE_DIRECTIVES} from '@angular/common';
import * as Chart from 'chart.js';

@Component({
  selector: 'chart',
  directives: [CORE_DIRECTIVES],
  styles: [require('./chart.scss')],
  template: `
  <div class="chart">
    <canvas #canvas></canvas>
  </div>
  `,
})

export class ChartDirective implements OnChanges{

  @Input() config:any = null;
  @ViewChild('canvas') canvas:ElementRef;

  chart:Chart;

  constructor(private el:ElementRef) {
  }

  ngOnChanges(changes:any) {
    if (!this.canvas || !this.config) return;
    let ctx = (this.canvas.nativeElement as HTMLCanvasElement).getContext('2d');
    this.chart = new Chart(ctx, this.config);
  }
}
