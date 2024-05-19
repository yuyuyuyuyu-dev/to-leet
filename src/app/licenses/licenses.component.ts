import { Component } from "@angular/core";
import _licenses from "../../assets/3rd-party-licenses.json";
import { RouterOutlet } from "@angular/router";
import { MatExpansionModule } from "@angular/material/expansion";

@Component({
  selector: "app-licenses",
  standalone: true,
  imports: [
    RouterOutlet,
    MatExpansionModule,
  ],
  templateUrl: "./licenses.component.html",
  styleUrl: "./licenses.component.css",
})
export class LicensesComponent {
  licenses = Object.values(_licenses);
}
