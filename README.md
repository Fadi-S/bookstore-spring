# Bookstore CMS API based on Spring (Backend)

[Frontend Bookstore CMS (based on React)](https://github.com/Fadi-S/bookstore-react.git)

## Download and Installation

To begin using this project, follow the following steps:

1. Clone the repository using the following command:
```bash
git clone https://github.com/Fadi-S/bookstore-spring.git bookstore-backend
```
2. Change to the project directory:
```bash
cd bookstore-backend
```
3. Create a `secret.properties` file in resources directory from `copy.secret.properties` and fill in the required fields.
```bash
cp src/main/resources/copy.secret.properties src/main/resources/secret.properties
```
4. Set up the database credentials in `persistence-mysql.properties` file.
5. Create a database in MySQL using database_structure.sql
6. (Optional) Seed the database with some data using `database_seed.sql` file.
7. (Optional) if you used the `database_seed.sql` file, you can use the following credentials to login as Admin:
    - Email: `admin@fadisarwat.dev`
    - Password: `admin`
8. (Optional) id you used the `database_seed.sql` file, you must copy the images in `books` to `src/main/resources/static/images/books` to have the images displayed correctly.
7. Install the project dependencies from the `pom.xml` file:
8. Start the development server by running BookstoreApplication.main()
9. Test endpoints using Postman or any other API testing tool.

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://god.gw.postman.com/run-collection/16240348-020f088e-2597-4c07-b585-6d9f95c030eb?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D16240348-020f088e-2597-4c07-b585-6d9f95c030eb%26entityType%3Dcollection%26workspaceId%3D7ca6c65b-5633-412b-87f8-b3154eac67c4)


## Features
### For users
1. Search, filter, and sort the list of books easily.
2. Add books to cart if you have an account.
3. View the details of a book.
4. Review a purchased book and view other users reviews.
5. View your profile and update your information.
6. Manage your cart and checkout (With Stripe integration).
7. View your orders

### For Admins
1. Add, update, and delete books.
2. View the list of orders and their details.
3. Update the status of an order.
4. Update quantities of books in stock.