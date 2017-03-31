package gov.epa.oeca.cgp.domain.dto;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;

import java.util.List;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
public class CgpNoiFormSearchResult {

    private List<CgpNoiForm> data;

    private Long draw;

    private Long recordsTotal;

    private Long recordsFiltered;

    public CgpNoiFormSearchResult(List<CgpNoiForm> data, Long draw, Long recordsTotal, Long recordsFiltered) {
        this.data = data;
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
    }

    public CgpNoiFormSearchResult() {
    }

    public List<CgpNoiForm> getData() {
        return data;
    }

    public void setData(List<CgpNoiForm> data) {
        this.data = data;
    }

    public Long getDraw() {
        return draw;
    }

    public void setDraw(Long draw) {
        this.draw = draw;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    @Override
    public String toString() {
        return "CgpNoiFormSearchResult{" +
                "recordsFiltered=" + recordsFiltered +
                ", recordsTotal=" + recordsTotal +
                ", draw=" + draw +
                ", data=" + data +
                '}';
    }
}
