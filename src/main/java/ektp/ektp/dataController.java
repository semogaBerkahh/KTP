/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ektp.ektp;

import ektp.ektp.exceptions.NonexistentEntityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author MSI GF75
 */
@Controller
public class dataController {
    DataJpaController datactrl = new DataJpaController ();
    List<Data> newdata = new ArrayList<>();
    
    @RequestMapping("/data")
    //@ResponseBody
    public String getDataKTP(Model model){
         int record = datactrl.getDataCount();
        String result="";
        try{
            newdata = datactrl.findDataEntities().subList(0, record);
        }
        catch (Exception e){
            result = e.getMessage();
        }
        model.addAttribute("goData", newdata);
        model.addAttribute("record", record);
        return "database";
    }
    
    @RequestMapping("/detail/{id}")
        public String detail(@PathVariable int id, Model model){
        
        Data data = new Data();
        
        try{
            data = datactrl.findData(id);
        }catch(Exception e){
            
        }
        
        String foto = "";
        if(data != null){
            foto = Base64.encodeBase64String(data.getFoto());
            model.addAttribute("foto", foto);
        }
        
        model.addAttribute("data", data);
        
        return "/detail";
    }
        
    @RequestMapping("/main")
    public String getMain() {
        return "menu";
    }

      
    @RequestMapping("/create")
    public String create(){
     
        return "buat";
    }
    
    @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RedirectView newDummyData(@RequestParam("gambar") MultipartFile f, Model m, HttpServletRequest r)
            throws ParseException, Exception{

        Data d = new Data();

        int id = Integer.parseInt(r.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tgllahir"));
        byte[] img = f.getBytes();

        String noktp = r.getParameter("noktp");
        String nama = r.getParameter("nama");
        String jk = r.getParameter("jeniskelamin");
        String alamat = r.getParameter("alamat");
        String agama = r.getParameter("agama");
        String status = r.getParameter("status");
        String pekerjaan = r.getParameter("pekerjaan");
        String warganegara = r.getParameter("warganegara");
        String berlaku = r.getParameter("berlakuhingga");
        
        d.setId(id);
        d.setNoktp(noktp);
        d.setNama(nama);
        d.setTgllahir(date);
        d.setJeniskelamin(jk);
        d.setAlamat(alamat);
        d.setAgama(agama);
        d.setStatus(status);
        d.setPekerjaan(pekerjaan);
        d.setWarganegara(warganegara);
        d.setBerlakuhingga(berlaku);
        d.setFoto(img);
        
        datactrl.create(d);
        
        return new RedirectView("/data");

    }
    
    @RequestMapping(value = "/gmbr", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
        Data d = datactrl.findData(id);
        byte[] gmbr = d.getFoto();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(gmbr);
    }
    
    @GetMapping("/delete/{id}")
    public String deleteData(@PathVariable("id") int id)  throws NonexistentEntityException, ektp.ektp.exceptions.NonexistentEntityException {

        datactrl.destroy(id);
        return "redirect:/data";
    }
    
    @RequestMapping("/edit/{id}")
       //@ResponseBody
       public String update(@PathVariable(value = "id") int id, Model model) throws NonexistentEntityException,  ektp.ektp.exceptions.NonexistentEntityException {
       Data d = datactrl.findData(id);
       model.addAttribute("data", d);
       return "/editdata";
             
    }
       
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
       public RedirectView updateData(@RequestParam("gambar") MultipartFile f, HttpServletRequest r)
       throws ParseException, Exception {
       
        Data d = new Data();    

        int id = Integer.parseInt(r.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tgllahir"));
        byte[] img = f.getBytes();
        
        String noktp = r.getParameter("noktp");
        String nama = r.getParameter("nama");
        String jk = r.getParameter("jeniskelamin");
        String alamat = r.getParameter("alamat");
        String agama = r.getParameter("agama");
        String status = r.getParameter("status");
        String pekerjaan = r.getParameter("pekerjaan");
        String warganegara = r.getParameter("warganegara");
        String berlaku = r.getParameter("berlakuhingga");
        
        d.setId(id);
        d.setNoktp(noktp);
        d.setNama(nama);
        d.setTgllahir(date);
        d.setJeniskelamin(jk);
        d.setAlamat(alamat);
        d.setAgama(agama);
        d.setStatus(status);
        d.setPekerjaan(pekerjaan);
        d.setWarganegara(warganegara);
        d.setBerlakuhingga(berlaku);
        d.setFoto(img);
        




        datactrl.edit(d);
        return new RedirectView("/data");
    }
}
