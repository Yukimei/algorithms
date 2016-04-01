package controller;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import databean.Customer;
import databean.PositionBean;
import databean.PositionViewBean;
import formbean.IdForm;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;
import model.Model;
import model.PositionDAO;
import model.CustomerDAO;
import model.FundDAO;
import model.PriceHistoryDAO;
import model.TransactionDAO;


public class ViewAccountAction extends Action {
    private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);
    private CustomerDAO customerDAO;
    private TransactionDAO transactionDAO;
    private PositionDAO positionDAO;
    private PriceHistoryDAO priceHistoryDAO;
    private FundDAO fundDAO;

    public ViewAccountAction(Model model) {
        customerDAO  = model.getCustomerDAO();
        positionDAO = model.getPositionDAO();
        transactionDAO = model.getTransactionDAO();
        fundDAO = model.getFundDAO();
        priceHistoryDAO = model.getPriceHistoryDAO();
    }

    public String getName() { return "view_account.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        try{
            HttpSession session = request.getSession();
            boolean isCustomer = (boolean) session.getAttribute("isCustomer");
            Customer customer = (Customer) session.getAttribute("customer");

            //if is customer, there should be related  information set in the session.
            if (isCustomer) {
                if (customer == null) {
                 errors.add("No user is login");
                 return "login-customer.jsp";
                }
            } else {
                IdForm form = formBeanFactory.create(request);
                Customer customerBean = customerDAO.read(form.getIdAsInt());
                
                errors.addAll(form.getValidationErrors());
                
                if (errors.size() != 0) {
                    return "customer-list.jsp?page=1";
                }
                
                if (customerBean == null) {
                    errors.add("Account does not exist!");
                    return "customer-list.jsp?page=1";
                }
                
                session.setAttribute("customer", customerBean);
            }
            
            customer = (Customer) session.getAttribute("customer");
            
            int customerId = customer.getCustomerId();

            request.setAttribute("positionList", getPositionList(customer.getCustomerId()));
            request.setAttribute("valueList",getValueList(customer.getCustomerId()));
            Date date = transactionDAO.getLastDate(customerId);
            session.setAttribute("date", date);
            
            request.setAttribute("customer", customer);
            session.setAttribute("customer", customer);
            
            return "view-account.jsp";


        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }

    private double[] getValueList(int customerId) throws RollbackException {
    	 PositionBean[] positionBeans = positionDAO.getPositionListByCustomerId(customerId);
         double[] valueList = new double[positionBeans.length];
         
         for (int i = 0; i < positionBeans.length; i++) { 
             long shares = positionBeans[i].getShares();
             
             int fundid = positionBeans[i].getFundId();
             double price = priceHistoryDAO.getPriceByFundId(fundid);
             
             valueList[i] = price * shares;
         }
         return valueList;
    }
    public PositionViewBean[] getPositionList(int customerId) throws RollbackException {
        PositionBean[] positionBeans = positionDAO.getPositionListByCustomerId(customerId);
        PositionViewBean[] positionViewBeans = new PositionViewBean[positionBeans.length];     
        for (int i = 0; i < positionBeans.length; i++) { 
            PositionViewBean positionBean = new PositionViewBean();
            positionBean.setFundId(positionBeans[i].getFundId());
            positionBean.setfundName(fundDAO.read(positionBeans[i].getFundId()).getName());
            positionBean.setSymbol(fundDAO.read(positionBeans[i].getFundId()).getSymbol());
            positionBean.setPendingShares(positionBeans[i].getPendingShares());
            positionBean.setShares(positionBeans[i].getShares());
            int fundid = positionBeans[i].getFundId();
            positionBean.setPrice(priceHistoryDAO.getPriceByFundId(fundid));   
            positionBean.setValue(positionBeans[i].getShares() * priceHistoryDAO.getPriceByFundId(positionBeans[i].getFundId()));
            positionViewBeans[i] = positionBean;
        }
        return positionViewBeans;
    }
}
