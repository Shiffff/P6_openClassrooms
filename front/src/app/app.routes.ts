import { Routes } from "@angular/router";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/login/login.component";
import { RegisterComponent } from "./components/register/register.component";
import { NotFoundComponent } from "./components/not-found/not-found.component";
import { ArticlesComponent } from "./components/articles/articles.component";
import { authGuard } from "./guards/auth.guard";
import { ThemesComponent } from "./components/themes/themes.component";
import { ProfilComponent } from "./components/profil/profil.component";
import { NewArticleComponent } from "./components/new-article/new-article.component";
import { ArticleComponent } from "./components/article/article.component";

export const routes: Routes = [
    { path: "home", component: HomeComponent },

    { path: "login", component: LoginComponent },
    { path: "register", component: RegisterComponent },

    { path: "", component: ArticlesComponent, canActivate: [authGuard] },
    { path: "themes", component: ThemesComponent, canActivate: [authGuard] },
    { path: "profil", component: ProfilComponent, canActivate: [authGuard] },
    { path: "new-article", component: NewArticleComponent, canActivate: [authGuard] },
    { path: "article/:id", component: ArticleComponent, canActivate: [authGuard] },

    { path: "**", component: NotFoundComponent },
];
