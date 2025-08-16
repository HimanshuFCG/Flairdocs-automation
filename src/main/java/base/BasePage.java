package base;

import com.microsoft.playwright.Page;



public class BasePage {
    public Page page;

    public BasePage(Page page){
        this.page = page;
    }
}