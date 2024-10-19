import { Component } from "@angular/core";
import { ToLeetService } from "../to-leet.service";
import { FormControl, ReactiveFormsModule } from "@angular/forms";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Clipboard } from "@angular/cdk/clipboard";
import { ToLeetPipe } from "../to-leet.pipe";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";

@Component({
  selector: "app-home",
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    ToLeetPipe,
  ],
  templateUrl: "./home.component.html",
  styleUrl: "./home.component.scss",
})
export class HomeComponent {
  str = new FormControl("");

  constructor(
    private clipboard: Clipboard,
    private toLeetService: ToLeetService,
    private snackbar: MatSnackBar,
  ) { }

  onCopyToClipboardButtonClicked() {
    const copyWasSuccess = this.clipboard.copy(
      this.toLeetService.toLeet(this.str.value!!),
    );
    this.openSnackbar(
      copyWasSuccess ? "コピーしました" : "コピーできませんでした",
    );
  }

  private openSnackbar(message: string) {
    this.snackbar.open(
      message,
      undefined,
      { duration: 2500 },
    );
  }
}
