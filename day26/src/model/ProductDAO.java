package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAO {
	static final String sql_update="UPDATE PRODUCT SET CNT=CNT-1 WHERE NUM=?";
	static final String sql_selectOne="SELECT NUM,NAME,CNT,PRICE FROM PRODUCT WHERE NUM=?";
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;

	private boolean insert(ProductVO pVO) {
		return false;
	}
	// 상품구매
	public boolean update(ProductVO pVO) {


		conn=JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_update);
			pstmt.setInt(1, pVO.getNum());
			int result=pstmt.executeUpdate();
			if(result<=0) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
		JDBCUtil.disconnect(rs, pstmt, conn);
		return true;
	}
	private boolean delete(ProductVO pVO) {
		return false;
	}

	// 상품목록출력
	// 상품검색
	public ArrayList<ProductVO> selectAll(ProductVO pVO){

		final String sql_selectAll_NAME="SELECT NUM,NAME,CNT,PRICE FROM PRODUCT WHERE NAME LIKE '%' ? '%'";
		final String sql_selectAll_PRICE="SELECT NUM,NAME,CNT,PRICE FROM PRODUCT WHERE ?<=PRICE AND PRICE<=?";
		final String sql_selectAll="SELECT NUM,NAME,CNT,PRICE FROM PRODUCT WHERE NAME LIKE '%' ? '%' AND ?<=PRICE AND PRICE<=?";

		ArrayList<ProductVO> pdatas=new ArrayList<ProductVO>();

		conn=JDBCUtil.connect();
		try {
			if(pVO.getSearchCondition().equals("이름")) {
				pstmt = conn.prepareStatement(sql_selectAll_NAME);
				pstmt.setString(1, pVO.getName());
				rs=pstmt.executeQuery();
			}
			else if(pVO.getSearchCondition().equals("가격")) {
				pstmt = conn.prepareStatement(sql_selectAll_PRICE);
				pstmt.setInt(1, pVO.getPrice());
				pstmt.setInt(2, pVO.getTmpNum());
				rs=pstmt.executeQuery();
			}
			else if(pVO.getSearchCondition().equals("이름가격")) { // 이름도 찾으면서 AND 가격도 최저가 최고가 사이 
				pstmt = conn.prepareStatement(sql_selectAll);
				pstmt.setString(1, pVO.getName());
				pstmt.setInt(2, pVO.getPrice());
				pstmt.setInt(3, pVO.getTmpNum());
				rs=pstmt.executeQuery();
			}

			while(rs.next()) {
				pdatas.add(new ProductVO(rs.getInt("NUM"),rs.getString("NAME"),rs.getInt("CNT"),rs.getInt("PRICE")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
		JDBCUtil.disconnect(rs, pstmt, conn);

		return pdatas;
	}
	// 상품선택(상품구매 시)
	public ProductVO selectOne(ProductVO pVO){


		ProductVO pdata=null;

		conn=JDBCUtil.connect();
		try {
			pstmt = conn.prepareStatement(sql_selectOne);
			pstmt.setInt(1, pVO.getNum());
			rs=pstmt.executeQuery();

			if(rs.next()) {
				pdata=new ProductVO(rs.getInt("NUM"),rs.getString("NAME"),rs.getInt("CNT"),rs.getInt("PRICE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
		JDBCUtil.disconnect(rs, pstmt, conn);

		return pdata;
	}
}
