package org.tron.core.services.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.core.Wallet;
import org.tron.core.capsule.TransactionCapsule;
import org.tron.protos.Contract.ApproveContract;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.Protocol.Transaction.Contract.ContractType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;


@Component
@Slf4j(topic = "API")
public class ApproveServlet extends HttpServlet {

  @Autowired
  private Wallet wallet;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) {

  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      String contract = request.getReader().lines()
          .collect(Collectors.joining(System.lineSeparator()));
      logger.warn("XXXXXXXXXXXXXX Apply approve transaction step1, string is {}.", contract);
      ApproveContract.Builder build = ApproveContract.newBuilder();
      logger.warn("XXXXXXXXXXXXXX Apply approve transaction step2.");
      JsonFormat.merge(contract, build);
      logger.warn("XXXXXXXXXXXXXX Apply approve transaction step3.");
      //byte[] address = wallet.createAdresss(build.getValue().toByteArray());
      Transaction tx = wallet.createTransactionCapsule(build.build(), ContractType.ApproveContract)
          .getInstance();
      logger.warn("XXXXXXXXXXXXXX Apply approve transaction step4.");

      response.getWriter().println(Util.printTransaction(tx));
    } catch (Exception e) {
      try {
        response.getWriter().println(Util.printErrorMsg(e));
      } catch (IOException ioe) {
      }
    }
  }
}
