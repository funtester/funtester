package org.funtester.plugin.report.testng;

import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

/**
 * DateTime XML adapter
 * 
 * @author Thiago Delgado Pinto
 *
 */
@XmlTransient
public class DateTimeXmlAdapter extends XmlAdapter< Date, DateTime > {

    @Override
    public DateTime unmarshal(Date date) throws Exception {
        return new DateTime(date.getTime());
    }

    @Override
    public Date marshal(DateTime dateTime) throws Exception {
        return new Date(dateTime.getMillis());
    }
}
