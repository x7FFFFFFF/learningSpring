/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: BOF VIP-Service
 *
 * $Id: PagerModel.java 2018-11-14 9:33 tkachenko $
 *****************************************************************/
package com.noname.learningSpring.models;

public class PagerModel {

    private int buttonsToShow = 5;
    private int startPage;
    private int endPage;

    public PagerModel(int totalPages, int currentPage, int buttonsToShow) {
        setButtonsToShow(buttonsToShow);
        int halfPagesToShow = getButtonsToShow() / 2;

        if (totalPages <= getButtonsToShow()) {
            setStartPage(1);
            setEndPage(totalPages);
        } else if (currentPage - halfPagesToShow <= 0) {
            setStartPage(1);
            setEndPage(getButtonsToShow());
        } else if (currentPage + halfPagesToShow == totalPages) {
            setStartPage(currentPage - halfPagesToShow);
            setEndPage(totalPages);
        } else if (currentPage + halfPagesToShow > totalPages) {
            setStartPage(totalPages - getButtonsToShow() + 1);
            setEndPage(totalPages);
        } else {
            setStartPage(currentPage - halfPagesToShow);
            setEndPage(currentPage + halfPagesToShow);
        }

    }

    private int getButtonsToShow() {
        return this.buttonsToShow;
    }

    private void setButtonsToShow(int buttonsToShow) {
        if (buttonsToShow % 2 != 0) {
            this.buttonsToShow = buttonsToShow;
        } else {
            throw new IllegalArgumentException("Even buttons. WTF?!");
        }
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PagerModel{");
        sb.append("startPage=").append(startPage);
        sb.append(", endPage=").append(endPage);
        sb.append('}');
        return sb.toString();
    }
}
