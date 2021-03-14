# catalogManagementApplication
Catalog Management Software Application


*****************Catalog Management Software Application*****************

Environment : This application comes with Docker image and it has the relevant yaml file which can be deployed in any cloud environment using kubectl commands

Memory : Used in memory for storage of the data, hence not required any database to run this application

Docker Location : "kbellad/catalogmanagement", this can be downloaded using docker hub

Introduction : This API provides a set of API's for consumer application to do the following operations

1. Create a category : This API will create a category in the system

URL : http://$env_ip_addr:8088/category/

Method : POST

Headers :
Content-type : application/json

Input Payload : 

{
   "name":"$categoryName",
   "desc":"$categoryName description",
   "subCategory":{
      "name":"$subcategoryName name",
      "desc":"$subcategoryName description"
   },
   "products":{
   	"$productName": {
   "productName":"$productName",
   "desc":"$productName desc",
   "price": $price
   }
   }
}


Validations : 

Scenario : When you are trying to add the existed category

Error Message : Category name already exist! 


2. Update Category : This API will update the category details in the system

URL : http://$env_ip_addr:8088/category/$categoryName

Method : PUT

Input Payload :

{
   "name":"$updatedcategoryName",
   "desc":"$updatedcategoryName description",
   "subCategory":{
      "name":"$subcategoryName name",
      "desc":"$subcategoryName description"
   },
   "products":{
   	"$productName": {
   "productName":"$productName",
   "desc":"$productName desc",
   "price": $price
   }
   }
} 

Validations : 

Scenario : When you are trying to update the existed category

Error Message : Category name already exist! 


3. Search Category : This API will list the category details from the system


URL : http://$env_ip_addr:8088/category/$categoryName

Method : GET

Validations : 

Scenario : When you are trying to fetch the invalid category

Error Message : No records found for the given category name : $categoryName


4. Delete Category : This API will delete the category from the system

URL : http://$env_ip_addr:8088/category/$categoryName

Method : DELETE

Validations : 

Scenario : When you are trying to delete the invalid category

Error Message : Category $categoryName not present, hence cannot remove

Scenario : Removing category when the category has any products under it or under it's sub categories

Error Message : Category cannot be removed as it contains products

or 

Category cannot be removed as it contains products in the subcategory


5. Add a Category under existing category : This API will add the category into the existing category

URL : http://$env_ip_addr:8088/category/$categoryName

Method : PUT

{
   "name":"$categoryName",
   "desc":"$categoryName description",
   "subCategory":{
      "name":"$subcategoryName name",
      "desc":"$subcategoryName description"
   },
   "products":{
   	"$productName": {
   "productName":"$productName",
   "desc":"$productName desc",
   "price": $price
   }
   }
}


Validations : 

Scenario : When you are trying to add the existed category

Error Message : Category name already exist! 


6. Add Product : Thi API will create a Product to the existing Category in ths system

URL : http://$env_ip_addr:8088//product

Method : POST

Headers :
Content-type : application/json

Input Payload : 

{
   "name":"productName",
   "desc":"productName desc",
   "price": 33.33,
   "categoryName":"category2"
} 


Validations : 

Scenario : When you are trying to add the existed product

Error Message : Product name already exist!  



7. Update Product : This API will update the Product details in the system


URL : http://$env_ip_addr:8088/product?categoryName=$categoryName&productName=$productName

Method : PUT

Headers :
Content-type : application/json

Input Payload : 

{
   "name":"productName",
   "desc":"productName desc",
   "price": 33.33,
   "categoryName":"category2"
}  


Validations : 

Scenario : When you are trying to update the existed product

Error Message : Product name already exist! 


8. Search Product : This API will list the product details along with the category details from the system


URL : http://$env_ip_addr:8088/product?categoryName=category2&productName=product


Method : GET 


Validations : 

Scenario : When you are trying to fetch the invalid product

Error Message : $productName product not found

Scenario : When you are trying to fetch with invalid category

Error Message : Category $categoryName not present, hence cannot show the product


9. Delete Product : This API will delete the record from the system

URL : http://$env_ip_addr:8088/product?categoryName=$categoryName&productName=$productName

Method : DELETE

Headers :
Content-type : application/json

Validations : 

Scenario : When you are trying to delete with invalid category

Error Message : Category $categoryName not present, hence cannot remove the product


10. Move Products : This API will move the products from one category to another


URL : http://$env_ip_addr:8088/product/moveProducts?sourceCategorytName=$sourcecategoryName&destinationCategoryName=$destinationCategoryName

Method : PUT

Headers :
Content-type : application/json

Input Payload : 
{
"$productName": {
   "productName":"$productName",
   "desc":"$productName desc",
   "price": $price
   }
}

Validations : 

Scenario : When you are trying to move empty product

Error Message : Empty products, cannot be moved


Validations : 

Scenario : When source or destination category is invalid

Error Message : Source or Destination category cannot be empty






