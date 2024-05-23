# Тайник повара
### Тема: Pet-проект - хранилище рецептов

### Стек технологий

- Spring (spring-boot)
- HTML + CSS + Thymeleaf (Шаблонизатор)
- СУБД H2

### Основной функционал:

- Просмотр существующих рецептов, которые добавляют пользователи
- Создание своего аккаунта
- Добавление собственного рецепта
- Поиск по рецептам и ингредиентам
- Реализация роли администратора

### Preview

![Image alt](https://github.com/ezhevichnyj-kot/CookingSite-ARCHIVE/blob/main/scr1.png)
![Image alt](https://github.com/ezhevichnyj-kot/CookingSite-ARCHIVE/blob/main/scr2.png)
![Image alt](https://github.com/ezhevichnyj-kot/CookingSite-ARCHIVE/blob/main/scr3.png)
![Image alt](https://github.com/ezhevichnyj-kot/CookingSite-ARCHIVE/blob/main/scr4.png)

### Дополнительно:

Обо всех недостатках проекта я знаю. Поскольку это pet-проект и мой первый опыт веб-разработки (делал в одиночку), исправлять их не вижу смысла. Вот исправления, которые необходимо сделать для релизной версии проекта:

#### Критические проблемы:
- Использовать CSRF токены (были отключены чтобы быстрее сделать MVP)
- Использовать хеширование паролей (не настроено - используется NoOpPasswordEncoder)
- Настроить CORS (@CrossOrigin)

#### Желательные:
- Вынести логику пагинации на сервер (сейчас она жёстко прописана в JS-скриптах)
- Часть функций сервисов можно заменить на автогенерируемые функции в репозиториях
- Использовать @RequestMapping и поправить URL пути к функциям
- Вынести header в templates/fragments
- Доделать дизайн страницы /admin

#### P.S.
- В принципе мой первый опыт с веб-разработкой, как front-end, так и back-end, так что не судите строго! Я считаю, что отлично сделал свой первый проект (без курсов, без помощи кого-либо, лишь по материалам из интернета)!
- Попробовал внести немного исправлений, но понял, что не хочу тратить пару дней на то, что врядли будет кем-то запущено хоть раз -> код с исправлениями лежит в ветке hotfixes (версия недоделана, поэтому много чего не работает полноценно)
