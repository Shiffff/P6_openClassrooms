import { Routes } from "@angular/router";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/login/login.component";
import { RegisterComponent } from "./components/register/register.component";
import { NotFoundComponent } from "./components/not-found/not-found.component";
import { ArticlesComponent } from "./components/articles/articles.component";
import { authGuard } from "./guards/auth.guard";
import { ThemesComponent } from "./components/themes/themes.component";
import { ProfilComponent } from "./components/profil/profil.component";

export const routes: Routes = [
    { path: "", component: HomeComponent },

    { path: "login", component: LoginComponent },
    { path: "register", component: RegisterComponent },

    { path: "articles", component: ArticlesComponent, canActivate: [authGuard] },
    { path: "themes", component: ThemesComponent, canActivate: [authGuard] },
    { path: "profil", component: ProfilComponent, canActivate: [authGuard] },

    { path: "**", component: NotFoundComponent },
];
