import {Component, ViewEncapsulation, ViewContainerRef} from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'sd-app',
  encapsulation: ViewEncapsulation.None,
  directives: [],
  template: `
  <route-view></route-view>
  `
})
export class AppComponent {
  viewContainerRef:any = null;

  public constructor(viewContainerRef:ViewContainerRef) {
    // You need this small hack in order to catch application root view container ref
    this.viewContainerRef = viewContainerRef;
  }

}
