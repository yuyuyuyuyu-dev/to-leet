import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatInputModule } from "@angular/material/input";
import { FormControl, ReactiveFormsModule } from "@angular/forms";
import { ToLeetService } from "./to-leet.service";
import { ToLeetPipe } from "./to-leet.pipe";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: "app-root",
  standalone: true,
  imports: [
    RouterOutlet,
    ReactiveFormsModule,
    MatToolbarModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    ToLeetPipe,
  ],
  templateUrl: "./app.component.html",
  styleUrl: "./app.component.css",
})
export class AppComponent {
  title = "to-leet";
  str = new FormControl("");

  constructor(
    private toLeetService: ToLeetService,
    private snackbar: MatSnackBar,
  ) { }

  onCopyToClipboardButtonClickedardButtonClicked() {
    navigator.clipboard.writeText(this.toLeetService.toLeet(this.str.value!!))
    this.openSnackbar("コピーしました")
  }

  private openSnackbar(message: string) {
    this.snackbar.open(
      message,
      undefined,
      { duration: 2500 },
    );
  }
}
