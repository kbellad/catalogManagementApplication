package com.demo.catalog.management;

import com.demo.catalog.management.domain.CatalogResponse;
import com.demo.catalog.management.domain.Category;
import com.demo.catalog.management.domain.Product;
import com.demo.catalog.management.domain.SubCategory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		CatalogManagementApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogManagementApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;


	private HttpClient httpClient;

	@PostConstruct
	public void init() {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	private HttpHeaders gethttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	@Test
	public void testAddCategory() {
		addCategory("TestCategory");
	}

	@Test
	public void testAddCategoryWithSubCategory() {
		SubCategory subCategory = new SubCategory("SubCategory","SubCategory Description",null,null);
		Category category = new Category("TestWithSubCategory","TestWithSubCategory Description",subCategory,null);
		HttpEntity<?> entity = new HttpEntity<>(category, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/category/", HttpMethod.POST, entity,
				String.class);
		assertEquals(201, response.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/TestWithSubCategory",
				HttpMethod.GET, entity, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestWithSubCategory"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getSubCategory().getName().equals("SubCategory"));
	}

	@Test
	public void testAddCategoryWithProducts() {
		Map<String, Product> products =  new HashMap<>();
		Product p = new Product("TestProduct","TestProduct Description",599.0,"TestCategory");
		products.put("TestProduct", p);
		Category category = new Category("TestCategoryWithProduct","TestCategoryWithProduct Description",null,products);
		HttpEntity<?> entity = new HttpEntity<>(category, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/category/", HttpMethod.POST, entity,
				String.class);
		assertEquals(201, response.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/TestCategoryWithProduct",
				HttpMethod.GET, entity, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestCategoryWithProduct"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getProducts().get("TestProduct") != null);
	}

	@Test
	public void testAddCategoryWithSubCatAndProducts() {
		Map<String, Product> products =  new HashMap<>();
		Product p = new Product("TestProduct","TestProduct Description",599.0,"TestCategory");
		products.put("TestProduct", p);
		SubCategory subCategory = new SubCategory("TestSubCategory","TestSubCategory Description",null,null);
		Category category = new Category("TestCategoryWithSubCatAndProduct","TestCategoryWithSubCatAndProduct Description",subCategory,products);
		HttpEntity<?> entity = new HttpEntity<>(category, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/category/", HttpMethod.POST, entity,
				String.class);
		assertEquals(201, response.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/TestCategoryWithSubCatAndProduct",
				HttpMethod.GET, entity, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestCategoryWithSubCatAndProduct"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getSubCategory().getName().equals("TestSubCategory"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getProducts().get("TestProduct") != null);
	}

	@Test
	public void testAddCategoryWithSubCatAndProducts1() {
		Map<String, Product> products =  new HashMap<>();
		Product p = new Product("TestProduct","TestProduct Description",599.0,"TestCategory");
		products.put("TestProduct", p);
		Map<String, Product> subProducts =  new HashMap<>();
		Product p1 = new Product("TestSubProduct","TestSubProduct Description",599.0,"TestSubCategory");
		subProducts.put("TestSubProduct", p1);
		SubCategory subCategory = new SubCategory("TestSubCategory","TestSubCategory Description",null,subProducts);
		Category category = new Category("TestCategoryWithSubCatAndProduct1","TestCategoryWithSubCatAndProduct1 Description",subCategory,products);
		HttpEntity<?> entity = new HttpEntity<>(category, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/category/", HttpMethod.POST, entity,
				String.class);
		assertEquals(201, response.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/TestCategoryWithSubCatAndProduct1",
				HttpMethod.GET, entity, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestCategoryWithSubCatAndProduct1"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getSubCategory().getName().equals("TestSubCategory"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getProducts().get("TestProduct") != null);
		Assert.assertTrue(actualResponse.getBody().getCategory().getSubCategory().getProducts().get("TestSubProduct") != null);
	}

	@Test
	public void testAddCategoryWithExistedName() {
		Category category = new Category("TestCategory","TestCategory Description",null,null);
		HttpEntity<?> entity = new HttpEntity<>(category, gethttpHeaders());
		ResponseEntity<CatalogResponse> response = testRestTemplate.exchange("/category/", HttpMethod.POST, entity,
				CatalogResponse.class);
		assertEquals(400, response.getStatusCodeValue());
	}

	@Test
	public void testUpdateCategory() {
		addCategory("TestCategoryForUpdate1");

		Category updateCategory = new Category("TestCategory_Update","TestCategory_Update Description",null,null);
		HttpEntity<?> updateEntity = new HttpEntity<>(updateCategory, gethttpHeaders());
		ResponseEntity<String> updateResponse = testRestTemplate.exchange("/category/TestCategoryForUpdate1", HttpMethod.PUT, updateEntity,
				String.class);
		assertEquals(200, updateResponse.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/TestCategory_Update",
				HttpMethod.GET, updateEntity, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestCategory_Update"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getDesc().equals("TestCategory_Update Description"));
	}

	@Test
	public void testUpdateCategoryWithExistedName() {
		addCategory("TestCategoryForUpdateExistedName");
		addCategory("TestCategoryForUpdateExistedName1");

		Category updateCategory = new Category("TestCategoryForUpdateExistedName1","TestCategoryForUpdateExistedName1 Description",null,null);
		HttpEntity<?> updateEntity = new HttpEntity<>(updateCategory, gethttpHeaders());
		ResponseEntity<String> updateResponse = testRestTemplate.exchange("/category/TestCategoryForUpdateExistedName", HttpMethod.PUT, updateEntity,
				String.class);
		assertEquals(400, updateResponse.getStatusCodeValue());
	}

	@Test
	public void testUpdateCategoryWithProductsAndSubCategory() {
		addCategory("TestCategoryForUpdate2");

		Map<String, Product> products =  new HashMap<>();
		Product p = new Product("TestProduct","TestProduct Description",599.0,"TestCategory");
		products.put("TestProduct", p);
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/TestCategoryForUpdate2",
				HttpMethod.GET, null, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestCategoryForUpdate2"));
		SubCategory updateSubCategory = new SubCategory("TestCategory_SubUpdate2","TestCategory_SubUpdate2 Description",null,products);
		Category updateCategory = new Category("TestCategory_Update2","TestCategory_Update2 Description",updateSubCategory,products);
		HttpEntity<?> updateEntity = new HttpEntity<>(updateCategory, gethttpHeaders());
		ResponseEntity<String> updateResponse = testRestTemplate.exchange("/category/TestCategoryForUpdate2", HttpMethod.PUT, updateEntity,
				String.class);
		assertEquals(200, updateResponse.getStatusCodeValue());
		actualResponse = testRestTemplate.exchange("/category/TestCategory_Update2",
				HttpMethod.GET, updateEntity, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestCategory_Update2"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getDesc().equals("TestCategory_Update2 Description"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getProducts().get("TestProduct") != null);
		Assert.assertTrue(actualResponse.getBody().getCategory().getSubCategory().getName().equals("TestCategory_SubUpdate2"));
	}

	@Test
	public void testSearchCategoryForInvalidCategory() {
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/XYZ",
				HttpMethod.GET, null, CatalogResponse.class);
		assertEquals(400, actualResponse.getStatusCodeValue());
	}

	@Test
	public void testRemoveCategory(){
		addCategory("RemoveCategory");

		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/RemoveCategory",
				HttpMethod.GET, null, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("RemoveCategory"));
		ResponseEntity<CatalogResponse> deleteResponse = testRestTemplate.exchange("/category/RemoveCategory",
				HttpMethod.DELETE, null, CatalogResponse.class);
		assertEquals(200, deleteResponse.getStatusCodeValue());
		ResponseEntity<CatalogResponse> getResponse = testRestTemplate.exchange("/category/RemoveCategory",
				HttpMethod.GET, null, CatalogResponse.class);
		assertEquals(400, getResponse.getStatusCodeValue());
	}

	@Test
	public void testRemoveCategoryWithInvalidName(){
		ResponseEntity<CatalogResponse> deleteResponse = testRestTemplate.exchange("/category/XYZ",
				HttpMethod.DELETE, null, CatalogResponse.class);
		assertEquals(400, deleteResponse.getStatusCodeValue());
	}

	@Test
	public void testAddProduct(){
		addCategory("TestCategoryProduct");
		addProductToCategory("TestCategoryProduct", null);
	}

	@Test
	public void testAddProductWithExistedName(){
		addCategory("TestCategoryProductExistedName");
		addProductToCategory("TestCategoryProductExistedName", null);
		addProductToCategoryForExistedName("TestCategoryProductExistedName","TestProduct");
	}

	@Test
	public void testUpdateProduct(){
		addCategory("TestCategoryProduct1");
		addProductToCategory("TestCategoryProduct1", null);

		Product updateProduct = new Product("TestUpdateProduct1","TestUpdateProduct1 Description",50.0,"TestCategoryProduct1");
		HttpEntity<?> entity = new HttpEntity<>(updateProduct, gethttpHeaders());
		ResponseEntity<CatalogResponse> updateResponse = testRestTemplate.exchange("/product?categoryName=TestCategoryProduct1&productName=TestProduct", HttpMethod.PUT, entity,
				CatalogResponse.class);
		assertEquals(200, updateResponse.getStatusCodeValue());

		ResponseEntity<CatalogResponse> getResponse = testRestTemplate.exchange("/product?categoryName=TestCategoryProduct1&productName=TestUpdateProduct1",
				HttpMethod.GET, entity, CatalogResponse.class);
		assertEquals(200, getResponse.getStatusCodeValue());
		Assert.assertTrue(getResponse.getBody().getProduct().getName().equals("TestUpdateProduct1"));
	}

	@Test
	public void testUpdateProductWithExistedName(){
		addCategory("TestCategoryProductExName");
		addProductToCategory("TestCategoryProductExName", null);
		addProductToCategory("TestCategoryProductExName", "TestProduct1");

		Product updateProduct = new Product("TestProduct1","TestProduct1 Description",50.0,"TestCategoryProductExName");
		HttpEntity<?> entity = new HttpEntity<>(updateProduct, gethttpHeaders());
		ResponseEntity<CatalogResponse> updateResponse = testRestTemplate.exchange("/product?categoryName=TestCategoryProductExName&productName=TestProduct", HttpMethod.PUT, entity,
				CatalogResponse.class);
		assertEquals(400, updateResponse.getStatusCodeValue());
	}

	@Test
	public void testDeleteProduct() {
		addCategory("TestCategoryProduct2");
		addProductToCategory("TestCategoryProduct2", null);

		ResponseEntity<CatalogResponse> deleteResponse = testRestTemplate.exchange("/product?categoryName=TestCategoryProduct2&productName=TestProduct",
				HttpMethod.DELETE, null, CatalogResponse.class);
		assertEquals(200, deleteResponse.getStatusCodeValue());
		ResponseEntity<CatalogResponse> getResponse = testRestTemplate.exchange("/product?categoryName=TestCategoryProduct2&productName=TestProduct",
				HttpMethod.GET, null, CatalogResponse.class);
		assertEquals(400, getResponse.getStatusCodeValue());
	}

	@Test
	public void testSearchProduct(){
		addCategory("TestCategoryProduct3");
		addProductToCategory("TestCategoryProduct3", null);

		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/product?categoryName=TestCategoryProduct3&productName=TestProduct",
				HttpMethod.GET, null, CatalogResponse.class);
		assertEquals(200, actualResponse.getStatusCodeValue());
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestCategoryProduct3"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getProducts().get("TestProduct") != null);
	}

	@Test
	public void testMoveProduct(){
		addCategory("TestMoveCategoryProduct1");
		Product product = new Product("TestProduct", "TestProduct Description", 50.0, "TestMoveCategoryProduct1");
		HttpEntity<?> entity = new HttpEntity<>(product, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/product/", HttpMethod.POST, entity,
				String.class);
		assertEquals(201, response.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/product?categoryName=TestMoveCategoryProduct1&productName=TestProduct",
				HttpMethod.GET, entity, CatalogResponse.class);
		assertEquals(200, actualResponse.getStatusCodeValue());
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestMoveCategoryProduct1"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getProducts().get("TestProduct") != null);

		addCategory("TestMoveCategoryProduct2");
		Map<String, Product> moveMap = actualResponse.getBody().getCategory().getProducts();
		HttpEntity<?> moveEntity = new HttpEntity<>(moveMap, gethttpHeaders());
		ResponseEntity<CatalogResponse> moveResponse = testRestTemplate.exchange("/product/moveProducts?sourceCategorytName=TestMoveCategoryProduct1&destinationCategoryName=TestMoveCategoryProduct2", HttpMethod.PUT, moveEntity,
				CatalogResponse.class);
		assertEquals(200, moveResponse.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse1 = testRestTemplate.exchange("/product?categoryName=TestMoveCategoryProduct1&productName=TestProduct",
				HttpMethod.GET, entity, CatalogResponse.class);
		assertEquals(400, actualResponse1.getStatusCodeValue());
	}

	@Test
	public void testDeleteCategoryWithProduct(){
		addCategory("TestDeleteCategoryWithProduct");
		addProductToCategory("TestDeleteCategoryWithProduct", null);

		ResponseEntity<CatalogResponse> deleteResponse = testRestTemplate.exchange("/category/TestDeleteCategoryWithProduct",
				HttpMethod.DELETE, null, CatalogResponse.class);
		assertEquals(400, deleteResponse.getStatusCodeValue());
		Assert.assertTrue(deleteResponse.getBody().getMessage().contains("Category cannot be removed as it contains products"));
	}

	@Test
	public void testDeleteCategoryWithSubCategory(){
		Map<String, Product> map = new HashMap<>();
		map.put("TestProduct", new Product("TestProduct","TestProduct Description",99.9,"SubCategory"));
		SubCategory subCategory = new SubCategory("SubCategory","SubCategory Description",null,map);
		Category category = new Category("TestWithSubCategory1","TestWithSubCategory1 Description",subCategory,null);
		HttpEntity<?> entity = new HttpEntity<>(category, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/category/", HttpMethod.POST, entity,
				String.class);
		assertEquals(201, response.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/TestWithSubCategory1",
				HttpMethod.GET, entity, CatalogResponse.class);
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals("TestWithSubCategory1"));
		Assert.assertTrue(actualResponse.getBody().getCategory().getSubCategory().getName().equals("SubCategory"));

		ResponseEntity<CatalogResponse> deleteResponse = testRestTemplate.exchange("/category/TestWithSubCategory1",
				HttpMethod.DELETE, entity, CatalogResponse.class);
		assertEquals(400, deleteResponse.getStatusCodeValue());
		Assert.assertTrue(deleteResponse.getBody().getMessage().equals("Category cannot be removed as it contains products in the subcategory"));
	}


	private void addCategory(String categoryName){
		Category category = new Category(categoryName,categoryName + " Description",null,null);
		HttpEntity<?> entity = new HttpEntity<>(category, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/category/", HttpMethod.POST, entity,
				String.class);
		assertEquals(201, response.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/category/" + categoryName,
				HttpMethod.GET, entity, CatalogResponse.class);
		assertEquals(200, actualResponse.getStatusCodeValue());
		Assert.assertTrue(actualResponse.getBody().getCategory().getName().equals(categoryName));
	}

	private void addProductToCategory(String categoryName, String productName){
		productName = productName == null ? "TestProduct" : productName;
		Product product = new Product(productName,productName + " Description",50.0,categoryName);
		HttpEntity<?> entity = new HttpEntity<>(product, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/product/", HttpMethod.POST, entity,
				String.class);
		assertEquals(201, response.getStatusCodeValue());
		ResponseEntity<CatalogResponse> actualResponse = testRestTemplate.exchange("/product?categoryName=" + categoryName + "&productName=" + productName,
				HttpMethod.GET, entity, CatalogResponse.class);
		assertEquals(200, actualResponse.getStatusCodeValue());
		Assert.assertTrue(actualResponse.getBody().getProduct().getName().equals(productName));
	}

	private void addProductToCategoryForExistedName(String categoryName, String productName){
		Product product = new Product(productName,productName + " Description",50.0,categoryName);
		HttpEntity<?> entity = new HttpEntity<>(product, gethttpHeaders());
		ResponseEntity<String> response = testRestTemplate.exchange("/product/", HttpMethod.POST, entity,
				String.class);
		assertEquals(400, response.getStatusCodeValue());
	}


}
