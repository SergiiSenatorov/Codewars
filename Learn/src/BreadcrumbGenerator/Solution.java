package BreadcrumbGenerator;

/*
http://www.codewars.com/kata/563fbac924106b8bf7000046/train/java

As breadcrumb menùs are quite popular today, I won't digress much on explaining them, leaving the wiki link to do all
the dirty work in my place.

What might not be so trivial is instead to get a decent breadcrumb from your current url. For this kata, your purpose
is to create a function that takes a url, strips the first part (labelling it always HOME) and then builds it making
each element but the last a <a> element linking to the relevant path; last has to be a <span> element getting the active
class.

All elements need to be turned to uppercase and separated by a separator, given as the second parameter of the function;
the last element can terminate in some common extension like .html, .htm, .php or .asp; if the name of the last element
is index.something, you treat it as if it wasn't there, sending users automatically to the upper level folder.

A few examples can be more helpful than thousands of words of explanation, so here you have them:

Solution.generate_bc("mysite.com/pictures/holidays.html", " : ").equals( '<a href="/">HOME</a> : <a href="/pictures/">PICTURES</a> : <span class="active">HOLIDAYS</span>' );
Solution.generate_bc("www.codewars.com/users/GiacomoSorbi", " / ").equals( '<a href="/">HOME</a> / <a href="/users/">USERS</a> / <span class="active">GIACOMOSORBI</span>' );
Solution.generate_bc("www.microsoft.com/docs/index.htm", " * ").equals( '<a href="/">HOME</a> * <span class="active">DOCS</span>' );
Seems easy enough?

Well, probably not so much, but we have one last extra rule: if one element (other than the root/home) is longer than
30 characters, you have to shorten it, acronymizing it (i.e.: taking just the initials of every word); url will be always
given in the format this-is-an-element-of-the-url and you should ignore words in this array while acronymizing:
["the","of","in","from","by","with","and", "or", "for", "to", "at", "a"];
a url composed of more words separated by - and equal or less than 30 characters long needs to be just uppercased with
hyphens replaced by spaces.

Ignore anchors (www.url.com#lameAnchorExample) and parameters (www.url.com?codewars=rocks&pippi=rocksToo) when present.

Examples:

Solution.generate_bc("mysite.com/very-long-url-to-make-a-silly-yet-meaningful-example/example.htm", " > ").equals( '<a href="/">HOME</a> > <a href="/very-long-url-to-make-a-silly-yet-meaningful-example/">VLUMSYME</a> > <span class="active">EXAMPLE</span>' );
Solution.generate_bc("www.very-long-site_name-to-make-a-silly-yet-meaningful-example.com/users/giacomo-sorbi", " + ").equals( '<a href="/">HOME</a> + <a href="/users/">USERS</a> + <span class="active">GIACOMO SORBI</span>' );
You will always be provided valid url to webpages in common formats, so you probably shouldn't bother validating them.

If you like to test yourself with actual work/interview related kata, please also consider this one about building a
string filter for Angular.js.

Special thanks to the colleague that, seeing my code and commenting that I worked on that as if it was I was on CodeWars,
made me realize that it could be indeed a good idea for a kata :)
 */

import java.util.ArrayList;
import java.util.Arrays;

public class Solution
{
    public static void main(String[] args)
    {
        //System.out.println(generate_bc("mysite.com/pictures/holidays.html", " : "));
        //System.out.println(generate_bc("www.codewars.com/users/GiacomoSorbi", " / "));
        //System.out.println(generate_bc("www.microsoft.com/docs/index.htm", " * "));
        //System.out.println(generate_bc("mysite.com/very-long-url-to-make-a-silly-yet-meaningful-example/example.htm", " > "));
        //System.out.println(generate_bc("www.very-long-site_name-to-make-a-silly-yet-meaningful-example.com/users/giacomo-sorbi", " + "));
        //System.out.println(generate_bc("www.codewars.com/users/GiacomoSorbi?ref=CodeWars","\\" ));
        System.out.println(generate_bc("twitter.de/profiles/research-meningitis-or-bioengineering/secret-page.html"," - "));
        System.out.println(generate_bc("https://www.linkedin.com/in/giacomosorbi"," * "));
    }

    public static String generate_bc(String url, String separator)
    {
        StringBuilder res = new StringBuilder("<a href=\"/\">HOME</a>");
        // Отсекаем доменную часть
        url = url.substring(url.indexOf('/') + 1,url.length());
        // Обрабатываем поддомены (разделы)
        while(url.indexOf('/') > -1)
        {
            String subdom = url.substring(0,url.indexOf('/'));
            url = url.substring(url.indexOf('/') + 1, url.length());
            String abrSubdom = "";
            // Если subdom содержит # или ? - то это спан и финал
            if(subdom.contains("?") || subdom.contains("#"))
            {
                subdom = subdom.replace("#","?");
                subdom = subdom.substring(0,subdom.indexOf("?")).toUpperCase();
                res.append(separator + "<span class=\"active\">");
                res.append(subdom.replaceAll("[-_]"," "));
                res.append("</span>");
                return res.toString();
            }
            // Если длина subdom > 30 , его необходимо вывести в виде абреввиатуры
            if(subdom.length() > 30)
            {
                ArrayList<String> l = new ArrayList<>(Arrays.asList(subdom.split("[-_]")));
                ArrayList<String> el = new ArrayList<String>(Arrays.asList("the", "of", "in", "from", "by", "with", "and", "or", "for", "to", "at", "a"));

                for (String s : l)
                {
                    if (!el.contains(s))
                        abrSubdom += s.charAt(0);
                }
                abrSubdom = abrSubdom.toUpperCase();
            }
            // Стандартное окончание url
            if(url.indexOf("index.") == 0)
            {
                res.append(separator + "<span class=\"active\">");
                res.append(subdom.toUpperCase().replaceAll("[-_]"," "));
                res.append("</span>");
                return res.toString();
            }
            res.append(separator + "<a href=\"/");
            //res.append(subdom.replaceAll("[-_]"," "));
            if(abrSubdom.isEmpty())
            {
                res.append(subdom.replaceAll("[-_]"," "));
                res.append("/\">" + subdom.toUpperCase().replaceAll("[-_]"," ") + "</a>");
            }
            else
            {
                res.append(subdom);
                res.append("/\">" + abrSubdom.replaceAll("[-_]"," ") + "</a>");
            }
        }
        // Остаток - это span
        if(url.contains("?") || url.contains("#"))
        {
            url = url.replace("#","?");
            url = url.substring(0,url.indexOf("?")).toUpperCase();
            res.append(separator + "<span class=\"active\">");
            res.append(url.replaceAll("[-_]"," "));
            res.append("</span>");
            return res.toString();
        }
        res.append(separator + "<span class=\"active\">");
        if(url.indexOf(".") > 0)
            url = url.substring(0,url.indexOf("."));
        res.append(url.toUpperCase().replaceAll("[-_]"," "));
        res.append("</span>");
        return res.toString();
    }
}
