package cm4108.cw.location.model;


import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName="cm4108-cw")
public class Location{

private  String name;
private  double longitude,latitude;
private String date;

public Location(){} //end method

public Location(String name,double longitude,double latitude, String date){
this.setName(name);
this.setLongitude(longitude);
this.setLatitude(latitude);
this.setDate(date);
} //end method

//set table attributes

@DynamoDBHashKey(attributeName="name")
public String getName(){
	return name;
}
public void setName(String name){
	this.name = name;
}


@DynamoDBAttribute(attributeName="longitude")
public double getLongitude(){
	return longitude;
}
public void setLongitude(double longitude){
	this.longitude = longitude;
}


@DynamoDBAttribute(attributeName="latitude")
public double getLatitude(){
	return latitude;
}
public void setLatitude(double latitude){
	this.latitude = latitude;
}


@DynamoDBAttribute(attributeName="date")
public String getDate(){
	return date;
}
public void setDate(String date){
	this.date = date;
}

} //end class
