package cm4108.cw.Subscription.resource;

//general Java
import java.util.*;
//JAX-RS

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.amazonaws.regions.Regions;
//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//class imports
import cm4108.aws.util.*;
import cm4108.cw.Subscription.model.Subscription;
import cm4108.cw.config.*;

@SuppressWarnings("serial")

@Path("/subscription")
public class SubscriptionResource
{
@POST
@Produces(MediaType.TEXT_PLAIN)
public Response addASubscription(	@FormParam("name") String name,
									@FormParam("subname") String subname)
{
	
	
try	{
	Subscription subscription=new Subscription(name, subname);
	
	DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
	mapper.save(subscription);
	return Response.status(201).entity("Subscription Successfully saved").build();
	} catch (Exception e){
		return Response.status(400).entity("Error Saving Subscription").build();
		}
} //end method

@Path("/{subname}")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Collection<Subscription> retrieveSubscriptions(@PathParam("subname") String subname){
	
Map<String,AttributeValue> eav = new HashMap<String,AttributeValue>();
eav.put(":val", new AttributeValue().withS(subname));
	
DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
DynamoDBScanExpression scanExpression=new DynamoDBScanExpression().withFilterExpression("subscription = :val").withExpressionAttributeValues(eav);

List<Subscription> result=mapper.scan(Subscription.class, scanExpression);
return result;
} //end method

@GET
@Produces(MediaType.APPLICATION_JSON)
public Collection<Subscription> getAllSubscriptions(){
DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
DynamoDBScanExpression scanExpression=new DynamoDBScanExpression();	//create scan expression
List<Subscription> result=mapper.scan(Subscription.class, scanExpression);			//retrieve all subscriptions from DynamoDB
return result;
} //end method


@Path("/{name}")
@DELETE
public Response deleteSubscription(@PathParam("name") String name){
DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
Subscription subscription=mapper.load(Subscription.class,name);

if (subscription==null)
	throw new WebApplicationException(404);

mapper.delete(subscription);
return Response.status(200).entity("deleted").build();
} //end method
} //end class

