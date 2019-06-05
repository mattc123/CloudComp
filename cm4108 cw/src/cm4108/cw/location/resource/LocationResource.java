package cm4108.cw.location.resource;

//general Java
import java.util.*;
//JAX-RS

import javax.ws.rs.*;
import javax.ws.rs.core.*;

//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import cm4108.aws.util.*;
import cm4108.cw.location.model.*;
import cm4108.cw.config.*;

@SuppressWarnings("serial")

@Path("/location")
public class LocationResource
{
@POST
@Produces(MediaType.TEXT_PLAIN)

public Response addALocation(	@FormParam("name") String name,
								@FormParam("longitude") double longitude,
								@FormParam("latitude") double latitude,
								@FormParam("date") String date)
{
try	{
	Location location=new Location(name,longitude,latitude,date);
	
	DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
	mapper.save(location);
	return Response.status(201).entity("user saved").build();
	} catch (Exception e){
		return Response.status(400).entity("error in saving user").build();
		}
} //end method

@Path("/{id}")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Location getOneCity(@PathParam("id") String id){
DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
Location location=mapper.load(Location.class,id);

if (location==null){
	
	Location newLocation=new Location(id,0,0, "none");
	mapper.save(newLocation);
	return newLocation;
	
}else{
	return location;

}
} //end method

@GET
@Produces(MediaType.APPLICATION_JSON)
public Collection<Location> getAllLocation(){
DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
DynamoDBScanExpression scanExpression=new DynamoDBScanExpression();	//create scan expression
List<Location> result=mapper.scan(Location.class, scanExpression);			//retrieve all locations from DynamoDB
return result;
} //end method

} //end class

