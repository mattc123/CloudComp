
package cm4108.cw.request.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName="sub_Request")
public class Request{

private String requestID;
private String name;
private String subscription;
private String date;

public Request(){} //end method

public Request(String name, String subscription, String date){
this.setName(name);
this.setSubscription(subscription);
this.setDate(date);
} //end method

//set table attributes


@DynamoDBHashKey(attributeName="requestID")
@DynamoDBAutoGeneratedKey
public String getRequestID(){
	return requestID;
}
public void setRequestID(String requestID){
	this.requestID = requestID;
} 


@DynamoDBAttribute(attributeName="name")
public String getName(){
	return name;
}
public void setName(String name){
	this.name = name;
}


@DynamoDBAttribute(attributeName="subscription")
public String getSubscription(){
	return subscription;
}
public void setSubscription(String subscription){
	this.subscription = subscription;
}


@DynamoDBAttribute(attributeName="date")
public String getDate(){
	return date;
}
public void setDate(String date){
	this.date = date;
}

} //end class