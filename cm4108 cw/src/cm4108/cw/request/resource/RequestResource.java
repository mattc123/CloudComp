package cm4108.cw.request.resource;

//general Java
import java.util.*;

//JAX-RS
import javax.ws.rs.*;
import javax.ws.rs.core.*;

//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import cm4108.aws.util.*;
import cm4108.cw.request.model.Request;
import cm4108.cw.config.*;

@SuppressWarnings("serial")

@Path("/request")
public class RequestResource{
@POST
@Produces(MediaType.TEXT_PLAIN)
public Response addARequest(	@FormParam("name") String name,
								@FormParam("subscription") String subscription,
								@FormParam("date") String date)
{
	
try	{
	Request request=new Request(name,subscription,date);
	DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
	mapper.save(request);
	return Response.status(201).entity("Friend Request Saved").build();
	} catch (Exception e){
		return Response.status(400).entity("Error Saving Friend Request").build();
		}
} //end method

@Path("/{subscription}")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Collection<Request> retrieveRequests(@PathParam("subscription") String subscription){
	
Map<String,AttributeValue> eav = new HashMap<String,AttributeValue>();
eav.put(":val", new AttributeValue().withS(subscription));
DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
DynamoDBScanExpression scanExpression=new DynamoDBScanExpression().withFilterExpression("subscription = :val").withExpressionAttributeValues(eav);

List<Request> result=mapper.scan(Request.class, scanExpression);
return result;

} //end method

@Path("/{requestID}")
@DELETE
public Response deleteRequest(@PathParam("requestID") String requestID){
	
DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
Request request=mapper.load(Request.class,requestID);

if (request==null)
	throw new WebApplicationException(404);

mapper.delete(request);
return Response.status(200).entity("deleted").build();
} //end method
} //end class

