package ProductLine.Test;

import java.io.IOException;
import java.util.Arrays;

import jmetal.util.JMException;

public class Test {
	public static  String Sort(String commaDelimitedStr) {
		String[] arr = commaDelimitedStr.split(",");
		Arrays.sort(arr);
		String[] newArr=new String[arr.length];
		StringBuilder sb=new StringBuilder();
		for (int i=0;i<arr.length;i++){
			   sb.append(arr[i] +",");
		}
		return sb.toString();
	}
	
	public static int getBinomial(int n) {
		  int x = 0;
		  for(int i = 0; i < n; i++) {
		    if(Math.random() < 0.5)
		      x++;
		  }
		  return x;
		}
	

	
	public static void main(String[] args) throws JMException, IOException,
	ClassNotFoundException, Exception {
	   for (int i = 0; i < 10000; i++) {
		System.out.println(getBinomial(5));
	}
	
	}
	public static void main1(String[] args) throws JMException, IOException,
	ClassNotFoundException, Exception {
	   String s1="EShop,Store_front,Catalog,Product_Information,Product_type,Eletronic_goods,Basic_information,Buy_paths,Shopping_cart,Inventory_management_policy,Cart_content_page,Checkout,Checkout_type,Guest_checkout,Taxation_options,Tax_gateways,CertiTAX,Payment_options,Payment_types,COD,Order_confirmation,Eletronic_page,Business_management,Order_management,Fulfillment,Eletronic_goods_fulfillment,File_repository,License_management,Administration,Content_management,Product_database_management,Presentation_options,General_layout_management,Store_administration,Site_search,Search_engine_registration,Domain_name_setup";
	   String s2="EShop,Store_front,Business_management,Catalog,Buy_paths,Product_Information,Product_type,Basic_information,Shopping_cart,Checkout,Order_confirmation,Inventory_management_policy,Cart_content_page,Checkout_type,Taxation_options,Payment_options,Payment_types,Order_management,Administration,Fulfillment,Content_management,Store_administration,Product_database_management,Presentation_options,General_layout_management,Site_search,Search_engine_registration,Domain_name_setup,Tax_gateways,Services,Custom_tax_gateway,Debit_card,Guest_checkout,Wish_list_save_after_session,Services_fulfillment,Mail,Wish_list,";
	   
	   System.out.println(Sort(s1));
	   System.out.print("=======");
	   System.out.println(Sort(s2));
	
	}
}
