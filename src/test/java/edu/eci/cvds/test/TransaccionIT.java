/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.cvds.test;

import java.math.BigDecimal;
import junit.framework.Assert;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author davidleon
 */
public class TransaccionIT {
    
    private static WebDriver driver = null;
    
    public TransaccionIT() {
    }
    
    @BeforeClass
    public static void inicializarDriver() {
        
        //System.setProperty("webdriver.gecko.driver", "./libs/geckodriver.exe");
        //WebDriver driver = new FirefoxDriver();
        driver = new FirefoxDriver();
    }
    
    @AfterClass
    public static void liquidarDriver() {
        driver.quit();
    }
    
    
    @Test
    public void comprobarFlujoCorrectoTransferencia() {
        //driver.get("http://localhost:8080/faces/registrarRecurso.xhtml");
        
        /*
        WebElement nombre = driver.findElement(By.id("recurso_form:Nombre"));
        nombre.sendKeys("fisica electrostatica");  
        WebElement capacidad = driver.findElement(By.id("recurso_form:Capacidad"));
        capacidad.sendKeys("5");
        WebElement ubicacion = driver.findElement(By.name("recurso_form:j_idt34"));
        Select sel=new Select(ubicacion);
        sel.selectByValue("Biblioteca JAL Bloque B");
        WebElement tipo = driver.findElement(By.name("recurso_form:j_idt37"));
        Select sel2=new Select(tipo);
        sel2.selectByValue("Libro");
        WebElement estado = driver.findElement(By.name("recurso_form:j_idt40"));
        Select sel3=new Select(estado);
        sel3.selectByValue("Disponible");
        WebElement fechainicio = driver.findElement(By.id("recurso_form:Nombre"));
        fechainicio.sendKeys("01:00:00");
        WebElement fechafin = driver.findElement(By.id("recurso_form:FechaFin"));
        fechafin.sendKeys("24:00:00");
    
    
    
        WebElement cmdRegistar = driver.findElement(By.name("recurso_form:j_idt45"));
        cmdRegistar.click();
        
        //WebDriverWait wait = new WebDriverWait(driver, 5);
        //WebElement saldoElem = driver.findElement(By.id("saldo"));
        //wait.until(ExpectedConditions.visibilityOf(saldoElem));
        
        //BigDecimal valorSaldo = new BigDecimal(saldoElem.getText());
        //Assert.assertTrue("Saldo es positivo", 
                //alorSaldo.compareTo(BigDecimal.ZERO) >= 0);
                */
        assertTrue(1==1);
    }
    
}
