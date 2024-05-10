export interface article {
    id: number;
    title: string;
    content: string;
    author: string;
    themeId: number;
    themeName: string;
    updatedAt: string;
    createdAt: string;
    comments: comment[];
}

export interface comment {
    articleId: number;
    author: string;
    content: string;
}
export interface newComment {
    articleId: string;
    content: string;
}
