package org.tron.core.services.http;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.common.utils.ByteArray;
import org.tron.core.Wallet;
import org.tron.core.db.Manager;
import org.tron.protos.Protocol.Approve;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;


@Component
@Slf4j(topic = "API")
public class GetApproveServlet extends HttpServlet {

  @Autowired
  private Wallet wallet;

  @Autowired
  private Manager dbManager;

  private String convertOutput(Approve approve) {
    // convert asset id
    if (approve.getApproveAddress().isEmpty()) {
      return JsonFormat.printToString(approve);
    } else {
      JSONObject approveJson = JSONObject.parseObject(JsonFormat.printToString(approve));
      String ownerAddress = approveJson.get("owner_address").toString();
      approveJson.put(
              "owner_address", ownerAddress /*ByteString.copyFrom(ByteArray.fromHexString(ownerAddress)).toStringUtf8()*/);
      String approveAddress = approveJson.get("approve_address").toString();
      approveJson.put(
          "approve_address", approveAddress/*ByteString.copyFrom(ByteArray.fromHexString(approveAddress)).toStringUtf8()*/);
      return approveJson.toJSONString();
    }

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      String address = request.getParameter("owneraddress");
      Approve.Builder build = Approve.newBuilder();
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("ownerAddress", address);
      JsonFormat.merge(jsonObject.toJSONString(), build);
      logger.warn("XXXXXXXXXX get approve");
      Approve reply = wallet.getApprove(build.build());
      if (reply != null) {
        response.getWriter().println(convertOutput(reply));
      } else {
        response.getWriter().println("{}");
      }
    } catch (Exception e) {
      logger.debug("Exception: {}", e.getMessage());
      try {
        response.getWriter().println(Util.printErrorMsg(e));
      } catch (IOException ioe) {
        logger.debug("IOException: {}", ioe.getMessage());
      }
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      String account = request.getReader().lines()
          .collect(Collectors.joining(System.lineSeparator()));
      Approve.Builder build = Approve.newBuilder();
      JsonFormat.merge(account, build);

      Approve reply = wallet.getApprove(build.build());
      if (reply != null) {
        response.getWriter().println(convertOutput(reply));
      } else {
        response.getWriter().println("{}");
      }
    } catch (Exception e) {
      logger.debug("Exception: {}", e.getMessage());
      try {
        response.getWriter().println(Util.printErrorMsg(e));
      } catch (IOException ioe) {
        logger.debug("IOException: {}", ioe.getMessage());
      }
    }
  }
}
