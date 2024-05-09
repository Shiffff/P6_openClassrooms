import { Component, inject } from "@angular/core";
import { ThemesService } from "../../services/themes.service";
import { CommonModule } from "@angular/common";
import { tap } from "rxjs";

@Component({
    selector: "app-themes",
    standalone: true,
    imports: [CommonModule],
    templateUrl: "./themes.component.html",
    styleUrl: "./themes.component.scss",
})
export class ThemesComponent {
    private themesService = inject(ThemesService);

    themes$ = this.themesService.getThemes();

    subscribe(themeId: any) {
        this.themesService
            .themeSubscribe(themeId)
            .pipe(
                tap(() => {
                    this.themes$ = this.themesService.getThemes();
                }),
            )
            .subscribe();
    }

    unsubscribe(themeId: any) {
        this.themesService
            .themeUnsubscribe(themeId)
            .pipe(
                tap(() => {
                    this.themes$ = this.themesService.getThemes();
                }),
            )
            .subscribe();
    }
}
